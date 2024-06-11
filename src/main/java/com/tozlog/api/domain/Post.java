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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserAccount userAccount;

    @Builder
    private Post(String title, String content, UserAccount userAccount){
        this.title = title;
        this.content = content;
        this.userAccount = userAccount;
    }


    public PostResponse toResponse(){
        return PostResponse.builder()
                .postId(getId()).title(getTitle()).content(getContent())
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

    public Long getUserId() {
        return this.userAccount.getId();
    }
}
