package com.tozlog.api.request.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class CommentCreate {

    @Length(min = 1, max = 10, message = "작성자는 1~8 글자로 입력해주세요")
    @NotBlank(message = "작성자를 입력해주세요")
    private String author;

    @Length(min = 1, max = 500, message = "댓글은 1~500 글자로 입력해주세요")
    @NotBlank(message = "글 내용을 입력해주세요")
    private String content;

    @Length(min = 5, max = 30, message = "비밀번호는 5~30 글자로 입력해주세요")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Builder
    public CommentCreate(String author, String content, String password) {
        this.author = author;
        this.content = content;
        this.password = password;
    }
}
