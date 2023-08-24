package com.grandp.data.security.captcha;

import com.grandp.data.security.captcha.ICaptchaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class ReCaptchaFilter extends UsernamePasswordAuthenticationFilter {

  private CaptchaService reCaptchaService = new CaptchaService();

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    String recaptchaResponse = request.getParameter("g-recaptcha-response");
      reCaptchaService.processResponse(recaptchaResponse);
    return super.attemptAuthentication(request, response);
  }
}