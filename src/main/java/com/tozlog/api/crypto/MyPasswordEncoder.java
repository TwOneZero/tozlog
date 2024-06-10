package com.tozlog.api.crypto;


import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyPasswordEncoder {

    public static final SCryptPasswordEncoder passwordEncoder =
        new SCryptPasswordEncoder(
                16,
                8,
                1,
                32,
                64);




    public String encrypt(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean isMatch(String rawPassword, String encryptedPassword) {
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
