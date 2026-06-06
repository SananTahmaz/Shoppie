package com.shoppie.exceptions;

public class IncompatibleStatusChangeException extends RuntimeException {
    public IncompatibleStatusChangeException(String message) {
        super(message);
    }
}
