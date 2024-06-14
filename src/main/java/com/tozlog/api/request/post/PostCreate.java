package com.tozlog.api.request.post;


import com.tozlog.api.domain.Post;
import com.tozlog.api.domain.UserAccount;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostCreate {

    @NotBlank(message = "글 제목을 제대로 입력해주세요")
    private String title;
    @NotBlank(message = "글 내용을 제대로 입력해주세요")
    private String content;

    @Builder
    private PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toPostEntity(UserAccount userAccount){
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .userAccount(userAccount)
                .build();
    }
}