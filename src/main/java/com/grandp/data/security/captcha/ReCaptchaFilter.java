package com.grandp.data.security.captcha;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class ReCaptchaFilter extends UsernamePasswordAuthenticationFilter {

  private CaptchaService reCaptchaService = new CaptchaService();

//  @Override
//  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//    String recaptchaResponse = request.getParameter("g-recaptcha-response");
//    reCaptchaService.processResponse(request, recaptchaResponse);
//
//    return new UsernamePasswordAuthenticationToken(
//      request.getParameter("username"),
//      request.getParameter("password")
//    );
//  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    String recaptchaResponse = request.getParameter("g-recaptcha-response");

    if (recaptchaResponse != null &&  request instanceof HttpServletRequest) {
      reCaptchaService.processResponse((HttpServletRequest) request, recaptchaResponse);
    }

    super.doFilter(request, response, chain);
  }
}