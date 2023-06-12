package com.grandp.data.exception.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class ExceptionHandlerRegister extends ResponseEntityExceptionHandler {

    @Bean
    public GlobalExceptionHandler userExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}