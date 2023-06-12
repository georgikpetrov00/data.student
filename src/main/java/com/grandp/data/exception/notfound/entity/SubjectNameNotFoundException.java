package com.grandp.data.exception.notfound.entity;

import jakarta.persistence.EntityNotFoundException;

public class SubjectNameNotFoundException extends EntityNotFoundException {

    public SubjectNameNotFoundException() {
        super();
    }

    public SubjectNameNotFoundException(String message) {
        super(message);
    }

    public SubjectNameNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

}
