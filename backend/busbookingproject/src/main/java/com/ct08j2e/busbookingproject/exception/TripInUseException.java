package com.ct08j2e.busbookingproject.exception;

public class TripInUseException extends RuntimeException {
    public TripInUseException(String message) {
        super(message);
    }
}