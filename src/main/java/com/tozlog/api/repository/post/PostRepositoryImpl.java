package com.tozlog.api.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tozlog.api.domain.Post;
import com.tozlog.api.request.post.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.*;

import static com.tozlog.api.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> getList(PostSearch postSearch) {
        Long totalCount = jpaQueryFactory.select(post.count())
                .from(post)
                .fetchFirst();

        if (totalCount == null) {
            totalCount = 0L;
        }

        List<Post> items = jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();

        return new PageImpl<>(items, postSearch.getPageable(), totalCount);
    }
}
