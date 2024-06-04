package com.tozlog.api.domain;

import com.tozlog.api.response.post.PostResponse;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;

    @Lob
    @Setter
    private String content;

    @Builder
    private Post(String title, String content){
        this.title = title;
        this.content = content;
    }


    public PostResponse toResponse(){
        return PostResponse.builder()
                .id(getId()).title(getTitle()).content(getContent())
                .build();
    }

    public PostEditor.PostEditorBuilder toEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
    }
}
