package com.tozlog.api.config;

import com.tozlog.api.domain.UserAccount;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserPrincipal extends User {

    private final Long userId;

    public UserPrincipal(UserAccount user) {
        super(user.getEmail(), user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN")
//                        new SimpleGrantedAuthority("ROLE_ADMIN"),
//                        new SimpleGrantedAuthority("WRITE")
                )
        );
        this.userId = user.getId();
    }
}
