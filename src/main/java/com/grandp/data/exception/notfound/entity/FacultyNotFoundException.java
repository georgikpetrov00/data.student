package com.grandp.data.exception.notfound.entity;

import jakarta.persistence.EntityNotFoundException;

public class FacultyNotFoundException extends EntityNotFoundException {

    public FacultyNotFoundException() {
        super();
    }

    public FacultyNotFoundException(String message) {
        super(message);
    }

    public FacultyNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

}
