package com.meta.instagram.service;

import com.meta.instagram.domain.dto.account.RegisterAccountDto;
import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.Image;
import com.meta.instagram.domain.entity.repository.AccountRepository;
import com.meta.instagram.domain.entity.repository.ImageRepository;
import com.meta.instagram.exception.DuplicateAccountException;
import com.meta.instagram.exception.UploadFileException;
import com.meta.instagram.service.aws.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

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
    private final BCryptPasswordEncoder passwordEncoder;
    public Long join(RegisterAccountDto registerAccountDto) throws UploadFileException, DuplicateAccountException {
        // 이메일로 중복 회원 체크
        Optional<Account> findByEmailResult = accountRepository.findByEmail(registerAccountDto.getEmail());
        if (findByEmailResult.isPresent()) {
            throw new DuplicateAccountException("[오류] 이메일이 중복되어 가입할 수 없습니다.");
        }

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

        // Account 프로필 이미지 필드에 Image 객체 할당하여 Account 객체 생성 + 비밀번호 암호화
        String encodePw = passwordEncoder.encode(registerAccountDto.getPassword());
        Account account = registerAccountDto.toEntity(image,encodePw);

        // Account 저장
        accountRepository.save(account);

        return account.getId();
    }
//    public Long login(LoginAccountDto loginAccountDto){
//
//    }

}
