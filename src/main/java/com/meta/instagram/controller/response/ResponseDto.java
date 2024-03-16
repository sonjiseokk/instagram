package com.meta.instagram.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private int code;
    private T data;

    public static <T> ResponseDto<T> success(HttpStatus code, T data) {
        return new ResponseDto<>(code.value() ,data);
    }
}
