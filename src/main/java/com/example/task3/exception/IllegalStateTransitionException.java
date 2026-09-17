package com.example.task3.exception;

public class IllegalStateTransitionException extends RuntimeException {
    public IllegalStateTransitionException() {
    }

    public IllegalStateTransitionException(String message) {
        super(message);
    }

    public IllegalStateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
