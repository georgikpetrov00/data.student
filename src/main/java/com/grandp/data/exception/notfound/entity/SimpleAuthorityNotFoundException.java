package com.grandp.data.exception.notfound.entity;

import jakarta.persistence.EntityNotFoundException;

public class SimpleAuthorityNotFoundException extends EntityNotFoundException {

    public SimpleAuthorityNotFoundException() {
        super();
    }

    public SimpleAuthorityNotFoundException(String message) {
        super(message);
    }

    public SimpleAuthorityNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

}
