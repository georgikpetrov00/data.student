package com.grandp.data.security.captcha;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@Component
@ComponentScan(basePackages = { "com.grandp.data.security.captcha" })
public class CaptchaConfig {
  @Bean
  public ClientHttpRequestFactory clientHttpRequestFactory() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(3 * 1000);
    factory.setReadTimeout(7 * 1000);
    return factory;
  }

  @Bean
  public RestOperations restTemplate() {
    RestTemplate restTemplate = new RestTemplate(this.clientHttpRequestFactory());
    return restTemplate;
  }

  @Bean
  public CaptchaSettings captchaSettings() {
    CaptchaSettings captchaSettings = new CaptchaSettings();
    return captchaSettings;
  }

  @Bean
  public ReCaptchaAttemptService reCaptchaAttemptService() {
    ReCaptchaAttemptService reCaptchaAttemptService = new ReCaptchaAttemptService();
    return reCaptchaAttemptService;
  }
}