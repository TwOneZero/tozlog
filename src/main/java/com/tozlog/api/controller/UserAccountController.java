package com.tozlog.api.controller;


import com.tozlog.api.config.UserPrincipal;
import com.tozlog.api.response.user_account.UserResponse;
import com.tozlog.api.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserAccountController {

    private final UserAccountService userService;

    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(userService.getUserProfile(userPrincipal.getUserId()));
    }
}
