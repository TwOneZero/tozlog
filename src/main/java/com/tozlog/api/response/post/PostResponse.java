package com.tozlog.api.response.post;

import java.time.LocalDate;

import com.tozlog.api.domain.Post;

import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponse {

    private Long postId;
    private String title;;
    private String content;
    private LocalDate regDate;

    @Builder
    public PostResponse(Post post){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.regDate = post.getRegDate().toLocalDate();
    }
}
