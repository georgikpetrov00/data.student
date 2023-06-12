package com.grandp.data.exception.notfound.runtime;

public class SemesterNotFoundException extends RuntimeException {

    public SemesterNotFoundException() {
    }

    public SemesterNotFoundException(String message) {
        super(message);
    }

    public SemesterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SemesterNotFoundException(Throwable cause) {
        super(cause);
    }
}
