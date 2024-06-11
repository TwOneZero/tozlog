package com.tozlog.api.repository;

import com.tozlog.api.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Boolean existsByEmail(String email);

    Optional<UserAccount> findByEmail(String email);
}
