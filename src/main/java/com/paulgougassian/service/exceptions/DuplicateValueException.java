package com.paulgougassian.service.exceptions;

public class DuplicateValueException extends RuntimeException{
    public DuplicateValueException() {
    }

    public DuplicateValueException(String message) {
        super(message);
    }

    public DuplicateValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateValueException(Throwable cause) {
        super(cause);
    }
}
