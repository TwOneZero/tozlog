package com.tozlog.api.service;

import com.tozlog.api.crypto.MyPasswordEncoder;
import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.exception.post.AlreadyExistEmailException;
import com.tozlog.api.exception.post.InvalidSigninInformation;
import com.tozlog.api.repository.UserAccountRepository;
import com.tozlog.api.request.Signup;
import com.tozlog.api.request.UserLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private MyPasswordEncoder encoder;

    @BeforeEach
    void clean() {
        userAccountRepository.deleteAll();
    }


    @Test
    @DisplayName("회원 가입 - 성공")
    void test1(){
        //Given
        Signup signup = Signup.builder()
                .name("김똥개")
                .email("ddonggae@mail.com")
                .password("12345")
                .build();
        //When
        authService.signup(signup);
        //Then
        assertEquals(1L, userAccountRepository.count());
        UserAccount user = userAccountRepository.findAll().iterator().next();
        assertEquals("김똥개", user.getName());
        assertEquals("ddonggae@mail.com", user.getEmail());
        assertTrue(encoder.isMatch("12345", user.getPassword()));
    }

    @Test
    @DisplayName("회원 가입 시 이메일 중복 - 에러 반환")
    void test2(){
        createUserAccount();
        //Given
        Signup signup = Signup.builder()
                .name("김똥개")
                .email("210@mail.com")
                .password("12345")
                .build();
        //When

        //Then
        assertThrows(AlreadyExistEmailException.class, () -> {
            authService.signup(signup);
        });
    }

    @Test
    @DisplayName("로그인 - 성공")
    void test3(){
        //Given
        createUserAccount();

        UserLogin loginRequest = UserLogin.builder()
                .email("210@mail.com")
                .password("12345")
                .build();

        //When
        Long userId = authService.signIn(loginRequest);

        //Then
        assertNotNull(userId);

    }

    @Test
    @DisplayName("로그인 - 비밀번호 불일치 시 에러 반환")
    void test4(){
        //Given
        createUserAccount();

        UserLogin loginRequest = UserLogin.builder()
                .email("210@mail.com")
                .password("1234")
                .build();

        //When & Then
        assertThrows(InvalidSigninInformation.class, () -> {
            authService.signIn(loginRequest);
        });
    }


    private UserAccount createUserAccount(){
        var password = encoder.encrypt("12345");
        return userAccountRepository.save(
                UserAccount.builder()
                        .email("210@mail.com")
                        .name("이원영")
                        .password(password)
                        .build()
        );
    }
}