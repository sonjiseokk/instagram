package com.meta.instagram.domain.entity.repository;

import com.meta.instagram.domain.dto.AccountDto;
import com.meta.instagram.domain.entity.Account;
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
        AccountDto accountDto = getAccountData();
        Account account = accountDto.toEntity();
        //when
        Account savedAccount = accountRepository.save(account);
        em.flush();
        em.clear();
        //then

        assertThat(account.getEmail()).isEqualTo(savedAccount.getEmail());
        assertThat(account.getNickname()).isEqualTo(savedAccount.getNickname());
        assertThat(account.getPassword()).isEqualTo(savedAccount.getPassword());
    }
    @Test
    @DisplayName("계정 삭제")
    @Transactional
    void 계정_삭제() throws Exception {
        //given
        AccountDto accountDto = getAccountData();
        Account account = accountDto.toEntity();
        accountRepository.save(account);
//        assertThat(accountRepository.findAll().size()).isEqualTo();
        //when
        accountRepository.delete(account);

        //then
        assertThat(accountRepository.findAll().size()).isEqualTo(0);
    }


    private static AccountDto getAccountData() {
        return AccountDto.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("pass")
                .build();
    }
}