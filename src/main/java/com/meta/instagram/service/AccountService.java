package com.meta.instagram.service;

import com.meta.instagram.domain.dto.account.RegisterAccountDto;
import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.Image;
import com.meta.instagram.domain.entity.repository.AccountRepository;
import com.meta.instagram.domain.entity.repository.ImageRepository;
import com.meta.instagram.exception.UploadFileException;
import com.meta.instagram.service.aws.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    private final S3UploadService s3UploadService;
    public Long join(RegisterAccountDto registerAccountDto) throws UploadFileException {
        // DTO로부터 멀티파트파일의 이미지
        MultipartFile multipartFile = registerAccountDto.getProfileImage();

        // S3에 해당 파일 저장
        String savedPathName;
        try {
            savedPathName = s3UploadService.saveFile(multipartFile);
        } catch (IOException | NullPointerException e) {
            throw new UploadFileException("[오류] 이미지가 서버에 정상적으로 저장되지 못했습니다.");
        }

        // S3 URL을 이용하여 Image 객체 빌드
        Image image = Image.builder()
                .path(savedPathName)
                .type(multipartFile.getContentType())
                .size(multipartFile.getSize())
                .build();

        // Account 프로필 이미지 필드에 Image 객체 할당하여 Account 객체 생성
        Account account = registerAccountDto.toEntity(image);

        // Account 저장
        accountRepository.save(account);

        return account.getId();
    }
//    public Long login(LoginAccountDto loginAccountDto){
//
//    }

}
