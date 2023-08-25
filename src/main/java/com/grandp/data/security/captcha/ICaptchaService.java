package com.grandp.data.security.captcha;

import com.grandp.data.security.captcha.exception.ReCaptchaInvalidException;
import jakarta.servlet.http.HttpServletRequest;

public interface ICaptchaService {
    
    default void processResponse(final String response) throws ReCaptchaInvalidException {}

    default void processResponse(HttpServletRequest request, final String response) throws ReCaptchaInvalidException {}

    default void processResponse(final String response, String action) throws ReCaptchaInvalidException {}
    
    String getReCaptchaSite();

    String getReCaptchaSecret();
}
