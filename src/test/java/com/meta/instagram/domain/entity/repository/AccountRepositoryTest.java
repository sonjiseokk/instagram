package com.meta.instagram.domain.entity.repository;

import com.meta.instagram.domain.dto.account.RegisterAccountDto;
import com.meta.instagram.domain.entity.Account;
import com.meta.instagram.domain.entity.Image;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(readOnly = true)
@DataJpaTest
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @PersistenceContext
    private EntityManager em;
    @Test
    @DisplayName("계정 생성")
    void 계정_생성() throws Exception {
        //given
        RegisterAccountDto registerAccountDto = getAccountData();
        Account account = registerAccountDto.toEntity(Image.getDefaultImage());
        //when
        Account savedAccount = accountRepository.save(account);

        //then

        assertThat(account.getEmail()).isEqualTo(savedAccount.getEmail());
        assertThat(account.getNickname()).isEqualTo(savedAccount.getNickname());
        assertThat(account.getPassword()).isEqualTo(savedAccount.getPassword());
    }

    private static RegisterAccountDto getAccountData() {
        return RegisterAccountDto.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("pass")
                .profileImage(null)
                .build();
    }
}