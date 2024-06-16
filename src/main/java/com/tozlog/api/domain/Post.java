package com.tozlog.api.domain;

import com.tozlog.api.response.post.PostResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Lob
    @Setter
    @Column(nullable = false)
    private String content;


    @Column(nullable = false)
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserAccount userAccount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;

    @Builder
    private Post(String title, String content, UserAccount userAccount){
        this.title = title;
        this.content = content;
        this.userAccount = userAccount;
        this.regDate = LocalDateTime.now();
    }


    public PostResponse toResponse(){
        return PostResponse.builder()
        .post(this)
        .build();
                // .postId(getId())
                // .title(getTitle())
                // .content(getContent())
                // .regDate(getRegDate())
                // .build();
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

    public void addComment(Comment comment) {
        comment.setPost(this);
        this.comments.add(comment);
    }
}
