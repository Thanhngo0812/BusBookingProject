package com.ct08j2e.busbookingproject.exception;

public class InUseResourceException extends RuntimeException {
    public InUseResourceException(String message) {
        super(message);
    }
}
