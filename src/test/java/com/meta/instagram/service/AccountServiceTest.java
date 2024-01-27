package com.meta.instagram.service;

import com.meta.instagram.domain.dto.account.RegisterAccountDto;
import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.repository.AccountRepository;
import com.meta.instagram.exception.UploadFileException;
import com.meta.instagram.service.aws.S3UploadService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountServiceTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private S3UploadService s3UploadService;
    @Autowired
    private AccountService accountService;
    MultipartFile testFile = new MockMultipartFile("test", "test.txt", "image/jpeg", new byte[100]);

    @Test
    @DisplayName("S3에 프로필 이미지를 업로드 후 정상가입")
    void S3에_프로필_이미지를_업로드_후_정상가입() throws Exception {
        //given
        RegisterAccountDto dto = getRegisterDto(testFile);

        //when
        Long joinedId = accountService.join(dto);
        //then
        Account findAccount = accountRepository.findById(joinedId).orElseThrow();

        assertThat(accountRepository.findAll().size()).isEqualTo(1);
        assertThat(findAccount.getId()).isEqualTo(joinedId);
    }

    @Test
    @DisplayName("S3에 프로필 이미지를 업로드하던 중 오류 발생")
    void S3에_프로필_이미지를_업로드하던_중_오류_발생() throws Exception {
        //given
        RegisterAccountDto dto = getRegisterDto(null);

        //when
        //then
        Assertions.assertThrows(UploadFileException.class, () -> accountService.join(dto));
    }

    private RegisterAccountDto getRegisterDto(MultipartFile multipartFile) {
        RegisterAccountDto dto = RegisterAccountDto.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("11")
                .profileImage(multipartFile)
                .build();
        return dto;
    }


}