package com.mindex.challenge.exception_handler;

public class GlobalNotFoundException extends RuntimeException {
    public GlobalNotFoundException(String message) {
        super(message);
    }
}
