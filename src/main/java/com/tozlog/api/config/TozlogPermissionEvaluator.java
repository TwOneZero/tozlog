package com.tozlog.api.config;

import com.tozlog.api.exception.post.PostNotFound;
import com.tozlog.api.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
public class TozlogPermissionEvaluator implements PermissionEvaluator {

    private final PostRepository postRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        var post = postRepository.findById((Long) targetId)
                .orElseThrow(PostNotFound::new);
        if (!post.getUserId().equals(userPrincipal.getUserId())) {
            log.error("[인가 실패] 사용자가 작성한 글이 아닙니다.");
            return false;
        }
        return true;
    }
}
