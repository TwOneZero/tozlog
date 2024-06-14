package com.tozlog.api.exception.comment;

import com.tozlog.api.exception.post.TozlogException;

public class CommentNotFound extends TozlogException {
    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public CommentNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 0;
    }
}
