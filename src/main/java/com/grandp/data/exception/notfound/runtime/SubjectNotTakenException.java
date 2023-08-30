package com.grandp.data.exception.notfound.runtime;

public class SubjectNotTakenException extends RuntimeException {

  public SubjectNotTakenException() {
  }

  public SubjectNotTakenException(String message) {
    super(message);
  }

  public SubjectNotTakenException(String message, Throwable cause) {
    super(message, cause);
  }

  public SubjectNotTakenException(Throwable cause) {
    super(cause);
  }
}
