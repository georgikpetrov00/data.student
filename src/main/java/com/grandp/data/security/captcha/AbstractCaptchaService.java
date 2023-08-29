package com.grandp.data.security.captcha;

import java.util.regex.Pattern;

import com.grandp.data.security.captcha.exception.ReCaptchaInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Service
public abstract class AbstractCaptchaService implements ICaptchaService{
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractCaptchaService.class);
    
    protected HttpServletRequest request;

    protected CaptchaSettings captchaSettings = captchaSettings();

    protected ReCaptchaAttemptService reCaptchaAttemptService = reCaptchaAttemptService();

    protected RestOperations restTemplate = restTemplate();

    protected static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    
    protected static final String RECAPTCHA_URL_TEMPLATE = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
    
    @Override
    public String getReCaptchaSite() {
        return captchaSettings.getSite();
    }

    @Override
    public String getReCaptchaSecret() {
        return captchaSettings.getSecret();
    }
  

    protected void securityCheck(HttpServletRequest request, final String response) {
        this.request = request;

        LOGGER.debug("Attempting to validate response {}", response);

        if (reCaptchaAttemptService.isBlocked(getClientIP())) {
            throw new ReCaptchaInvalidException("Client exceeded maximum number of failed attempts");
        }

        if (!responseSanityCheck(response)) {
            throw new ReCaptchaInvalidException("Please apply the re-captcha");
        }
    }

    protected boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    protected String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }


    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3 * 1000);
        factory.setReadTimeout(7 * 1000);
        return factory;
    }

    private RestOperations restTemplate() {
        RestTemplate restTemplate = new RestTemplate(this.clientHttpRequestFactory());
        return restTemplate;
    }

    private CaptchaSettings captchaSettings() {
        CaptchaSettings captchaSettings = new CaptchaSettings();
        return captchaSettings;
    }

    private ReCaptchaAttemptService reCaptchaAttemptService() {
        ReCaptchaAttemptService reCaptchaAttemptService = new ReCaptchaAttemptService();
        return reCaptchaAttemptService;
    }
}
