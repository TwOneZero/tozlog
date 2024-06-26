package com.tozlog.api.response.error;


import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Example
 * {
 * "code" : "400",
 * "message" : "잘못된 요청입니다.",
 * "validation" : {
 * "title" : "글 제목을 제대로 입력해주세요"
 * }
 * }
 */

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    private ErrorResponse(String code, String message, Map<String,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String field, String errorMessage) {
        this.validation.put(field, errorMessage);
    }

}
