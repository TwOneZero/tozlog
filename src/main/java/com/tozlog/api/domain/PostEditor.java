package com.tozlog.api.domain;


import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    private String title;
    private String content;

    @Builder
    private PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
