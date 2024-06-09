package com.tozlog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tozlog.api.domain.Session;
import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.repository.SessionRepository;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.request.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userAccountRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공 후 session 저장")
    void test() throws Exception {
        //Given
        UserAccount userAccount = createUserAccount();

        UserLogin request = UserLogin.builder()
                .email("210@mail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //When & Then
        mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isNoContent()) // 204
                .andDo(print());


        assertEquals(1L,sessionRepository.count());

    }

    @Test
    @DisplayName("로그인 성공 후 쿠키에 accessToken 을 담아 반환")
    void test5() throws Exception {
        //Given
        createUserAccount();

        UserLogin request = UserLogin.builder()
                .email("210@mail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(request);

//        ResponseCookie cookie = ResponseCookie.from("SESSION", session.getAccessToken())
//                .domain("localhost") // TODO : 서버 환경에 따른 분리 필요
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//

        //When & Then
        var cookie = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNoContent())
                .andExpect(cookie().exists("SESSION"))
                .andDo(print())
                .andReturn().getResponse().getCookie("SESSION");

        log.info(">>>> Coookie = {}", cookie);


    }


    @Test
    @DisplayName("로그인 후 권한이 필요한 url 로 접속 - /foo")
    void test2() throws Exception {
        //Given
        createUserAccount();
        UserLogin request = UserLogin.builder()
                .email("210@mail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(request);

        var cookie = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                        .andReturn().getResponse().getCookie("SESSION");
        assertNotNull(cookie);
        String accessToken = cookie.getValue();

        //When & Then
        mockMvc.perform(get("/posts/foo")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("로그인 후 검증이 되지 않은 세션 값으로 권한이 필요한 요청 불가")
    void test3() throws Exception {
        //Given
        UserAccount user = UserAccount.builder()
                .name("이원영")
                .email("210@mail.com")
                .password("1234")
                .build();

        Session session = user.addSession();
        userAccountRepository.save(user);

        //When & Then
        mockMvc.perform(get("/posts/foo")
                                .header("Authorization", session.getAccessToken() + "-other")
                                .contentType(MediaType.APPLICATION_JSON)
//                        .content(json)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }



    private UserAccount createUserAccount(){
        return userAccountRepository.save(
                UserAccount.builder()
                        .email("210@mail.com")
                        .name("이원영")
                        .password("1234")
                        .build()
        );
    }
}