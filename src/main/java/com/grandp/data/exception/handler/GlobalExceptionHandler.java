package com.grandp.data.exception.handler;

import com.grandp.data.exception.notfound.entity.SubjectNameNotFoundException;
import com.grandp.data.exception.notfound.entity.SubjectNotFoundException;
import com.grandp.data.exception.notfound.entity.UserNotFoundException;
import com.grandp.data.exception.notfound.runtime.SemesterNotFoundException;
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

    private static final String ERROR_MESSAGE = "errorMessage";

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

        request.setAttribute(ERROR_MESSAGE, message);
        return ResponseEntity.status(406).body(message.toString().trim());
    }

    @ExceptionHandler({
            SemesterNotFoundException.class,
            SubjectNameNotFoundException.class,
            SubjectNotFoundException.class,
            UserNotFoundException.class})
    public ResponseEntity<String> handleSubjectNameNotFoundException(HttpServletRequest request, Exception ex) {
        String exMsg = ex.getMessage();
        request.setAttribute(ERROR_MESSAGE, exMsg);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exMsg);
    }

}
