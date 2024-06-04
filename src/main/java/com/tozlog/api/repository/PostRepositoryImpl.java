package com.tozlog.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tozlog.api.domain.Post;
import com.tozlog.api.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.tozlog.api.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();

    }
}
