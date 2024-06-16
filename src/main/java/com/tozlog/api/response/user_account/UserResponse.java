package com.tozlog.api.response.user_account;


import com.tozlog.api.domain.UserAccount;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String name;

    public UserResponse(UserAccount userAccount) {
        this.id = userAccount.getId();
        this.name = userAccount.getName();
    }

    public static UserResponse from(UserAccount userAccount){
        return new UserResponse(userAccount);
    }

}
