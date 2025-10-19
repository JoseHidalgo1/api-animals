package com.example.animals.exception;

public class HabitatIdAlreadyExistsException extends RuntimeException {
    public HabitatIdAlreadyExistsException(String message) {
        super(message);
    }
}
