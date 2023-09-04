package com.grandp.data.security.captcha;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.grandp.data.security.captcha.exception.ReCaptchaInvalidException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class ReCaptchaFilter extends OncePerRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(ReCaptchaFilter.class);
  private CaptchaService reCaptchaService = new CaptchaService();

  @Override
  protected void doFilterInternal(HttpServletRequest request,
    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    LOG.info("ReCaptchaFilter was called...");

    Authentication authn = SecurityContextHolder.getContext().getAuthentication();

    if (request.getMethod().equals("POST") && authn == null) {
      String captcha = request.getParameter("g-recaptcha-response");

      try {
        reCaptchaService.processResponse(request, captcha);
      } catch (ReCaptchaInvalidException e) {
        String captchaErrorMessage = e.getMessage();
        String redirectUrl = request.getContextPath() + "/login?recaptcha_error=" + URLEncoder.encode(captchaErrorMessage, StandardCharsets.UTF_8);
        response.sendRedirect(redirectUrl);

        return;
      }
    }

    filterChain.doFilter(request, response);
  }

}