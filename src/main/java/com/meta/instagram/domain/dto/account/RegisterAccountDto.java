package com.meta.instagram.domain.dto.account;

import com.meta.instagram.domain.entity.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterAccountDto {
    private String email;
    private String username;
    private String nickname;
    private String password;

    @Builder
    public RegisterAccountDto(String email, String username, String nickname, String password) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }

    public Account toEntity(String encodePw) {
        return Account.builder()
                .email(this.getEmail())
                .nickname(this.nickname)
                .username(this.username)
                .password(encodePw)
                .build();
    }
}
