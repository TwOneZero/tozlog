package com.tozlog.api.controller;


import com.tozlog.api.config.AppConfig;
import com.tozlog.api.request.Signup;
import com.tozlog.api.request.UserLogin;
import com.tozlog.api.response.auth.SessionResponse;
import com.tozlog.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/login")
    public ResponseEntity<SessionResponse> login(@RequestBody UserLogin loginRequest) {
        //db에서 조회
        Long userId =  authService.signIn(loginRequest);

        SecretKey secretKey = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String jws = Jwts.builder()
                .subject(String.valueOf(userId))
                .signWith(secretKey)
                .issuedAt(new Date())
                .compact();

        // 토큰 응답
        return ResponseEntity.ok().body(
                SessionResponse.builder().accessToken(jws).build()
        );
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signupRequest) {
        authService.signup(signupRequest);
    }
}
