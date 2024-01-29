package com.meta.instagram.domain.dto.account;

import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.Image;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RegisterAccountDto {
    private String email;
    private String nickname;
    private String password;
    private MultipartFile profileImage;

    @Builder
    public RegisterAccountDto(String email, String nickname, String password, MultipartFile profileImage) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImage = profileImage;
    }

    public Account toEntity(Image profileImage, String encodePw) {
        return Account.builder()
                .email(this.getEmail())
                .nickname(this.nickname)
                .password(encodePw)
                .profileImage(profileImage)
                .build();
    }
}
