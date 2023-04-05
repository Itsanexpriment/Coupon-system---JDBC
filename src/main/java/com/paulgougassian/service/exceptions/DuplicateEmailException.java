package com.paulgougassian.service.exceptions;

public class DuplicateEmailException extends DuplicateValueException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
