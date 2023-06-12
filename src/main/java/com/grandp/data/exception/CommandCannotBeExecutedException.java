package com.grandp.data.exception;

public class CommandCannotBeExecutedException extends Exception {

    public CommandCannotBeExecutedException() {

    }

    public CommandCannotBeExecutedException(String message) {
        super(message);
    }

    public CommandCannotBeExecutedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandCannotBeExecutedException(Throwable cause) {
        super(cause);
    }
}
