package com.tozlog.api.exception.post;

public class InvalidSigninInformation extends TozlogException{

    private static final String MESSAGE = "로그인 정보와 일치하는 정보가 없습니다.";

    public InvalidSigninInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}