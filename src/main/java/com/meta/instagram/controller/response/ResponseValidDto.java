package com.meta.instagram.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.List;

@Getter
@AllArgsConstructor
public class ResponseValidDto<T> {
    private boolean success;
    private int code;
    private List<Error> data;

    public static <T> ResponseValidDto<T> print(HttpStatus code, BindingResult bindingResult) {
        List<Error> errorMessages = bindingResult.getFieldErrors().stream()
                .map(fe -> new Error(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return new ResponseValidDto<>(false, code.value(), errorMessages);
    }

    @Data
    private static class Error {
        private String field;
        private String message;

        public Error(final String field, final String message) {
            this.field = field;
            this.message = message;
        }
    }
}
