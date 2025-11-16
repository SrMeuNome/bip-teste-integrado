package com.example.backend.application.excepiton;

public class TransferFailedException extends RuntimeException {
    public TransferFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
