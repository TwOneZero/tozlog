package com.tozlog.api.exception.post;

import lombok.Getter;

@Getter
public class AlreadyExistEmailException extends TozlogException {

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public AlreadyExistEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
