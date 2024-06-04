package com.tozlog.api.exception.post;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class TozlogException extends RuntimeException {

    private final Map<String, String> validation = new HashMap<>();

    public TozlogException(String message) {
        super(message);
    }

    public TozlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

}
