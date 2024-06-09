package com.tozlog.api.controller;


import com.tozlog.api.request.UserLogin;
import com.tozlog.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Void> login(@RequestBody UserLogin loginRequest) {
        //db에서 조회
        String accessToken =  authService.signIn(loginRequest);

        // TODO : 쿠키 학습
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost") // TODO : 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
        return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
        // 토큰 응답
    }
}
