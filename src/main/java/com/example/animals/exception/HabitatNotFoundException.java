package com.example.animals.exception;

public class HabitatNotFoundException extends RuntimeException {
    public HabitatNotFoundException(String message) {
        super(message);
    }
}
