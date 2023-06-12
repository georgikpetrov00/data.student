package com.grandp.data.exception;

public class UpdateRequestCannotBeExecutedException extends Exception {

    public UpdateRequestCannotBeExecutedException() {
    }

    public UpdateRequestCannotBeExecutedException(String message) {
        super(message);
    }

    public UpdateRequestCannotBeExecutedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateRequestCannotBeExecutedException(Throwable cause) {
        super(cause);
    }
}
