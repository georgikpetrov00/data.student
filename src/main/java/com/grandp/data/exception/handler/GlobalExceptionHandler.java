package com.grandp.data.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getClass().getName() + ": " + ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundError(HttpServletRequest request) {
        return "redirect:/error";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstrainViolationException(HttpServletRequest request, ConstraintViolationException ex) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation cv : violations) {
            message.append(cv.getMessage()).append(System.lineSeparator());
        }

//        request.setAttribute("errorMessage", message);
        return ResponseEntity.status(406).body(message.toString().trim());
    }

}
