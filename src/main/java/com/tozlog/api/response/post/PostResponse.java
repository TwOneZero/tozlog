package com.tozlog.api.response.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponse {

    private Long postId;
    private String title;;
    private String content;

    @Builder
    private PostResponse(Long postId, String title, String content){
        this.postId = postId;
        this.title = title;
        this.content = content;
    }
}
