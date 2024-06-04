package com.tozlog.api.request;


import com.tozlog.api.domain.Post;
import com.tozlog.api.exception.post.InvalidRequest;
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

    public Post toPostEntity(){
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }
}