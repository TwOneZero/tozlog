package com.tozlog.api.response.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponse {

    private Long id;
    private String title;;
    private String content;

    @Builder
    private PostResponse(Long id, String title, String content){
        this.id= id;
        this.title = title;
        this.content = content;
    }
}
