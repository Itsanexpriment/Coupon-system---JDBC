package com.paulgougassian.service.exceptions;

public class DuplicateCouponException extends DuplicateValueException {
    public DuplicateCouponException() {
    }

    public DuplicateCouponException(String message) {
        super(message);
    }

    public DuplicateCouponException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateCouponException(Throwable cause) {
        super(cause);
    }
}
