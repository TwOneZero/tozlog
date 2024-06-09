package com.tozlog.api.repository;

import com.tozlog.api.domain.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session,Long> {
    Optional<Session> findByAccessToken(String accessToken);
}
