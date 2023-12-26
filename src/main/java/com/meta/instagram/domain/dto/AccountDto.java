package com.meta.instagram.domain.dto;

import com.meta.instagram.domain.entity.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountDto {
    private String email;
    private String nickname;
    private String password;
    @Builder
    public AccountDto(final String email, final String nickname, final String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public Account toEntity() {
        return Account.builder()
                .email(this.getEmail())
                .nickname(this.nickname)
                .password(this.password)
                .build();
    }
}
