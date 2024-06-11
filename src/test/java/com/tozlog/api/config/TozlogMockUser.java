package com.tozlog.api.config;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TozlogMockSecurityContext.class)
public @interface TozlogMockUser {

    String name() default "이원영";
    String email() default "210@mail.com";

    String password() default "12345";

    String role() default "ROLE_ADMIN";
}
