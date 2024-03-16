package com.meta.instagram.domain.dto.account;

import com.meta.instagram.domain.entity.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterAccountDto {
    @NotBlank(message = "필수 값이 비어있습니다.")
    @Size(max = 30)
    private String email;
    @NotBlank(message = "필수 값이 비어있습니다.")
    @Size(max = 20)
    private String nickname;
    @NotBlank(message = "필수 값이 비어있습니다.")
    @Size(max = 20)
    private String password;

    @Builder
    public RegisterAccountDto(final String email, final String nickname, final String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public Account toEntity(String encodePw) {
        return Account.builder()
                .email(this.getEmail())
                .nickname(this.nickname)
                .password(encodePw)
                .build();
    }
}
