package com.tozlog.api.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Signup {

    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Builder
    public Signup(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

}
