package com.shoppie.exceptions.user;

public class IncompatiblePasswordException extends RuntimeException {
    public IncompatiblePasswordException(String message) {
        super(message);
    }
}
