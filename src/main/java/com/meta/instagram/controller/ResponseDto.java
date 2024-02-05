package com.meta.instagram.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private int code;
    private T data;
    private Error error;

    public static <T> ResponseDto<T> success(HttpStatus code, T data) {
        return new ResponseDto<>(true, code.value() ,data, null);
    }


    public static <T> ResponseDto<T> successHeader(HttpStatus code,T data, HttpHeaders headers) {
        return new ResponseDto<>(true, code.value(), data, null);
    }

    public static <T> ResponseDto<T> fail(HttpStatus code, String message) {
        return new ResponseDto<>(false, code.value(),null, new Error(message));
    }

    @Getter
    @AllArgsConstructor
    static class Error {
        private String message;
    }
}
