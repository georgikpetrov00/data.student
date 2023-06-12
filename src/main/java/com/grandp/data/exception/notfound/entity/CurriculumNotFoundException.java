package com.grandp.data.exception.notfound.entity;

import jakarta.persistence.EntityNotFoundException;

public class CurriculumNotFoundException extends EntityNotFoundException {

    public CurriculumNotFoundException() {
        super();
    }

    public CurriculumNotFoundException(String message) {
        super(message);
    }

    public CurriculumNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

}
