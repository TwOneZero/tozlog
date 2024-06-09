package com.tozlog.api.exception.post;

public class Unauthorized extends TozlogException{

    private static final String MESSAGE = "인증이 필요한 요청입니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}