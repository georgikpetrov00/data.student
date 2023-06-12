package com.grandp.data.exception.notfound.entity;

import jakarta.persistence.EntityNotFoundException;

public class SubjectNotFoundException extends EntityNotFoundException {

    public SubjectNotFoundException() {
        super();
    }

    public SubjectNotFoundException(String message) {
        super(message);
    }

    public SubjectNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

}
