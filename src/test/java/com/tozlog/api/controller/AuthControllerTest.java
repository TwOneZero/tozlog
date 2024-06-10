package com.tozlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.request.Signup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    void clean() {
        userAccountRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 - 성공")
    void test4() throws Exception {
        //Given
        createUserAccount();

        Signup signup = Signup.builder()
                .name("김똥개")
                .email("ddonggae@mail.com")
                .password("12345")
                .build();

        String json = objectMapper.writeValueAsString(signup);
        //When & Then
        mockMvc.perform(post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 가입 - 중복이메일은 에러 반환")
    void test6() throws Exception {
        //Given
        createUserAccount();

        Signup signup = Signup.builder()
                .name("김똥개")
                .email("210@mail.com")
                .password("12345")
                .build();

        String json = objectMapper.writeValueAsString(signup);
        //When & Then
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."))
                .andDo(print());

    }



    private UserAccount createUserAccount(){
        return userAccountRepository.save(
                UserAccount.builder()
                        .email("210@mail.com")
                        .name("이원영")
                        .password("12345")
                        .build()
        );
    }
}