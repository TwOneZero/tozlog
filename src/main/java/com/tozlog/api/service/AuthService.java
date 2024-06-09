package com.tozlog.api.service;


import com.tozlog.api.domain.Session;
import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.exception.post.InvalidSigninInformation;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.request.UserLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;


    public String signIn(UserLogin login){
        UserAccount userAccount =  userAccountRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
        Session session =  userAccount.addSession();
        return session.getAccessToken();
    }
}
