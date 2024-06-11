package com.tozlog.api.config;

import com.tozlog.api.domain.UserAccount;
import com.tozlog.api.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;


@RequiredArgsConstructor
public class TozlogMockSecurityContext implements WithSecurityContextFactory<TozlogMockUser> {

    private final UserAccountRepository userAccountRepository;
    @Override
    public SecurityContext createSecurityContext(TozlogMockUser annotation) {
        var user = UserAccount.builder()
                .email(annotation.email())
                .name(annotation.name())
                .password(annotation.password())
                .build();

        userAccountRepository.save(user);

        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
        var principal = new UserPrincipal(user);
        var authToken = new UsernamePasswordAuthenticationToken(principal, user.getPassword(), List.of(role));
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        return context;
    }
}
