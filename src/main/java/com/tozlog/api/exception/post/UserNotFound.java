package com.tozlog.api.exception.post;

public class UserNotFound extends TozlogException{

    private static final String MESSAGE = "존재하지 않는 유저입니다.";

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
