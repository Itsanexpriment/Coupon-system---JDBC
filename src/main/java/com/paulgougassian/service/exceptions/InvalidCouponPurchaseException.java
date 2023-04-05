package com.paulgougassian.service.exceptions;

public class InvalidCouponPurchaseException extends RuntimeException {
    public InvalidCouponPurchaseException() {
    }

    public InvalidCouponPurchaseException(String message) {
        super(message);
    }

    public InvalidCouponPurchaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCouponPurchaseException(Throwable cause) {
        super(cause);
    }
}
