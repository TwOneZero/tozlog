package com.tozlog.api.request.post;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostEdit {

    @NotBlank(message = "글 제목을 제대로 입력해주세요")
    private String title;
    @NotBlank(message = "글 내용을 제대로 입력해주세요")
    private String content;

    @Builder
    private PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
