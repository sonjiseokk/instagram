package com.meta.instagram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.instagram.domain.dto.account.RegisterAccountDto;
import com.meta.instagram.domain.entity.repository.AccountRepository;
import com.meta.instagram.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AccountControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @AfterEach
    public void clean() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() throws Exception {
        //given
        RegisterAccountDto dto = RegisterAccountDto.builder()
                .email("test@gmail.com")
                .nickname("test")
                .password("test")
                .build();

        String url = "/api/user/register";

        ObjectMapper objectMapper = new ObjectMapper();
        String dtoAsString = objectMapper.writeValueAsString(dto);
        //when
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.nickname").value("test"));
        //then
    }

    @Test
    @DisplayName("회원가입 공백으로 인한 실패")
    void 회원가입_공백으로_인한_실패() throws Exception {
        //given
        RegisterAccountDto dto = RegisterAccountDto.builder()
                .email("test@gmail.com")
                .nickname("")
                .password("")
                .build();

        String url = "/api/user/register";

        ObjectMapper objectMapper = new ObjectMapper();
        String dtoAsString = objectMapper.writeValueAsString(dto);
        //when
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.msg").exists());
    }

    @Test
    @DisplayName("회원가입 중복회원으로 인한 실패")
    void 회원가입_중복회원으로_인한_실패() throws Exception {
        //given
        RegisterAccountDto dto = RegisterAccountDto.builder()
                .email("test@gmail.com")
                .nickname("")
                .password("")
                .build();

        // 첫 회원 가입
        accountService.join(dto);

        //when
        String url = "/api/user/register";
        ObjectMapper objectMapper = new ObjectMapper();
        String dtoAsString = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.msg").exists());

    }
}