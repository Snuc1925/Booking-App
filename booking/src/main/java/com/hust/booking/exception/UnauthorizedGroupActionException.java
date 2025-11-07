package com.hust.booking.exception;

public class UnauthorizedGroupActionException extends RuntimeException {
    public UnauthorizedGroupActionException(String message) {
        super(message);
    }
}