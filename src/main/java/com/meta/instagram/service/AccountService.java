package com.meta.instagram.service;

import com.meta.instagram.domain.entity.repository.AccountRepository;
import com.meta.instagram.domain.entity.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 계정과 관련된 서비스
 *
 * 1. 회원가입
 * 2. 로그인
 */
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final ImageRepository imageRepository;

//    public Long join(RegisterAccountDto registerAccountDto) {
//        MultipartFile multipartFile = registerAccountDto.getProfileImage();
//
//        registerAccountDto.toEntity()
//    }

}
