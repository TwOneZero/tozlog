package com.tozlog.api.exception.comment;

import com.tozlog.api.exception.post.TozlogException;

public class InvalidPassword extends TozlogException {

    private static final String MESSAGE = "패스워드가 잘못되었습니다.";

    public InvalidPassword() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
