package com.tozlog.api.repository;

import com.tozlog.api.domain.UserAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmailAndPassword(String email, String password);
}
