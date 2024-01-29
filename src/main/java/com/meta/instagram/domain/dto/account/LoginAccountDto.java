package com.meta.instagram.domain.dto.account;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginAccountDto {
    private String email;
    private String password;

    @Builder
    public LoginAccountDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
