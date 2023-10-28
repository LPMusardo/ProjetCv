package com.example.projetcv.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    // You can add additional constructors as needed
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
