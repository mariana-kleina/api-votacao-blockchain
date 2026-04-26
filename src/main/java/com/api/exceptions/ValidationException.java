package com.api.exceptions;

public class ValidationException extends ApiException {
    public ValidationException(String message) {
        super(message);
    }
}