package com.sh.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public enum ErrorCode {
    // 중복에러(아이디, 닉네임)
    ALREADY_EXISTS(HttpStatus.CONFLICT, "U_001", "Already exists.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
