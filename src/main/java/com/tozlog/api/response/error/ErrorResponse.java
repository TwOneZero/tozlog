package com.tozlog.api.response.error;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

@RequiredArgsConstructor
@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    public void addValidation(String field, String errorMessage) {
        this.validation.put(field, errorMessage);
    }


}
