package com.example.animals.exception;

public class AnimalIdAlreadyExistsException extends RuntimeException {
    public AnimalIdAlreadyExistsException(String message) {
        super(message);
    }
}