package com.meta.instagram.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.meta.instagram.domain.dto.account.RegisterAccountDto;
import com.meta.instagram.domain.dto.security.CustomAccountDetails;
import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.Image;
import com.meta.instagram.domain.entity.repository.AccountRepository;
import com.meta.instagram.domain.entity.repository.ImageRepository;
import com.meta.instagram.exception.DuplicateAccountException;
import com.meta.instagram.exception.UploadFileException;
import com.meta.instagram.service.aws.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    public Long join(RegisterAccountDto registerAccountDto) throws DuplicateAccountException {
        // 이메일로 중복 회원 체크
        Optional<Account> findByEmailResult = accountRepository.findByEmail(registerAccountDto.getEmail());
        if (findByEmailResult.isPresent()) {
            throw new DuplicateAccountException("[오류] 이메일이 중복되어 가입할 수 없습니다.");
        }
        // Account 비밀번호 암호화
        String encodePw = passwordEncoder.encode(registerAccountDto.getPassword());

        // registerAccountDto 메소드를 통해 엔티티로 변환
        Account account = registerAccountDto.toEntity(encodePw);

        // Account 저장
        accountRepository.save(account);

        return account.getId();
    }

    //    public Long login(LoginAccountDto loginAccountDto){
//
//    }
    public void profileImageUpload(Authentication authentication, MultipartFile multipartFile) throws UploadFileException {
        CustomAccountDetails accountDetails = (CustomAccountDetails) authentication.getPrincipal();
        Optional<Account> findAccount = accountRepository.findById(accountDetails.getId());

        if (findAccount.isEmpty()) {
            throw new NotFoundException("[오류] 인증 객체에서 유저 정보를 가져올 수 없습니다.");
        }
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

        Account account = accountDetails.getAccount();
        account.changeProfileImage(image);
    }

}
