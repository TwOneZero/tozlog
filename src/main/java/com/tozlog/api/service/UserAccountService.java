package com.tozlog.api.service;


import com.tozlog.api.exception.post.UserNotFound;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.response.user_account.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserResponse getUserProfile(Long userId) {
        return userAccountRepository.findById(userId)
                .map(UserResponse::from)
                .orElseThrow(UserNotFound::new);
    }

}
