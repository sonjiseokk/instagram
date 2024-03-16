package com.meta.instagram.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{

    public ValidationException(final String message) {
        super(message);
    }
}
