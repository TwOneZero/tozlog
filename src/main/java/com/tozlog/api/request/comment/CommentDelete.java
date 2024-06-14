package com.tozlog.api.request.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDelete {

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    public CommentDelete(){}

    @Builder
    public CommentDelete(String password) {
        this.password = password;
    }
}
