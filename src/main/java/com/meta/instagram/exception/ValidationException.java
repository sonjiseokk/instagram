package com.meta.instagram.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ValidationException extends RuntimeException{
    private BindingResult bindingResult;

    public ValidationException(final BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
