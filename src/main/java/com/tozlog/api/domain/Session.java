package com.tozlog.api.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private String accessToken;

    @Getter
    @ManyToOne
    private UserAccount userAccount;

    @Builder
    public Session(UserAccount userAccount) {
        this.accessToken = UUID.randomUUID().toString();
        this.userAccount = userAccount;
    }
}
