package com.shoppie.exceptions.user;

public class IncompatibleStatusChangeException extends RuntimeException {
    public IncompatibleStatusChangeException(String message) {
        super(message);
    }
}
