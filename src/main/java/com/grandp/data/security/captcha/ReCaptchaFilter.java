package com.grandp.data.security.captcha;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class ReCaptchaFilter extends OncePerRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(ReCaptchaFilter.class);

  private CaptchaService reCaptchaService = new CaptchaService();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    LOG.info("ReCaptchaFilter was called...");

    if (request.getMethod().equals("POST")) {
      String captcha = request.getParameter("g-recaptcha-response");
      reCaptchaService.processResponse(request, captcha);
    }

    filterChain.doFilter(request, response);
  }

}