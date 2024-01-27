package com.meta.instagram.domain.dto.account;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginAccountDto {
    private String userId;
    private String password;

    @Builder
    public LoginAccountDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

}
