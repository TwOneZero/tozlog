package com.tozlog.api.service;


import com.tozlog.api.crypto.MyPasswordEncoder;
import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.exception.post.AlreadyExistEmailException;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final MyPasswordEncoder encoder;

    public void signup(Signup signupRequest) {
        Boolean existUser = userAccountRepository.existsByEmail(signupRequest.getEmail());
        if (existUser) {
            throw new AlreadyExistEmailException();
        }

        var encodedPassword = encoder.encrypt(signupRequest.getPassword());

        var user = UserAccount.builder()
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .password(encodedPassword).build();
        userAccountRepository.save(user);
    }
}
