package com.meta.instagram.controller;

import com.meta.instagram.domain.dto.account.RegisterAccountDto;
import com.meta.instagram.exception.DuplicateAccountException;
import com.meta.instagram.exception.UploadFileException;
import com.meta.instagram.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AccountController {
    private final AccountService accountService;


    /**
     * 회원 가입을 진행하는 컨트롤러
     * @author 손지석
     *
     * @param dto 가입시에 입력할 정보
     * @return 성공시 이메일과 닉네임, 실패시 실패 메시지
     * @throws UploadFileException  프로필 이미지 업로드 실패시 오류 메시지 반환
     * @throws DuplicateAccountException 중복 가입 시도시 오류 메시지 반환
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterAccountDto dto) throws UploadFileException, DuplicateAccountException {
        try {
            // 가입 진행
            accountService.join(dto);
            // 정상 가입시 200 상태 코드와 입력한 이메일과 닉네임을 반환
            // 200 OK
            return ResponseEntity.status(OK)
                    .body(ResponseDto.success(OK, new ResponseRegisterUser(dto.getEmail(), dto.getNickname())));
        } catch (DuplicateAccountException e) {
            log.error(e.getMessage());
            // 중복 가입이기 때문에 가입을 진행할 수 없음.
            // 409 Conflict
            return ResponseEntity.status(CONFLICT)
                    .body(ResponseDto.fail(CONFLICT, e.getMessage()));
        } catch (UploadFileException e) {
            // 422 Unprocessable Entity
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(ResponseDto.fail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()));
        }
    }

    record ResponseRegisterUser(String email, String nickname) {
    }
}