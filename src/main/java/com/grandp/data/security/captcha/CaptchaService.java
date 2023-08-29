package com.grandp.data.security.captcha;

import java.net.URI;

import com.grandp.data.security.captcha.exception.ReCaptchaInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("captchaService")
public class CaptchaService extends AbstractCaptchaService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CaptchaService.class);

    @Override
    public void processResponse(HttpServletRequest request,final String response) {
        securityCheck(request, response);

        final URI verifyUri = URI.create(String.format(RECAPTCHA_URL_TEMPLATE, getReCaptchaSecret(), response, getClientIP()));

        final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
        LOGGER.debug("Google's response: {} ", googleResponse);

        if (!googleResponse.isSuccess()) {
            if (googleResponse.hasClientError()) {
                reCaptchaAttemptService.reCaptchaFailed(getClientIP());
            }
            throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
        }

        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
    }
}
