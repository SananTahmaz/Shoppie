package com.shoppie.exceptions;

public class IncompatiblePasswordException extends RuntimeException {
    public IncompatiblePasswordException(String message) {
        super(message);
    }
}
