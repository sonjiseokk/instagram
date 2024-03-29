package com.meta.instagram.exception.handler;

import com.meta.instagram.exception.DuplicateAccountException;
import com.meta.instagram.exception.ErrorResult;
import com.meta.instagram.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice("com.meta.instagram.controller")
public class AccountControllerAdvise {
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DuplicateAccountException.class)
    public ErrorResult duplicateAccountException(DuplicateAccountException e) {
        log.error("[예외] 중복 사용자 예외");
        return new ErrorResult(BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResult validationException(ValidationException e) {
        log.error("[예외] 잘못된 요청 예외");
        return new ErrorResult(BAD_REQUEST.value(), e.getMessage());
    }

    @Data
    @AllArgsConstructor
    static class ValidError{
        private String field;
        private String msg;
    }
}
