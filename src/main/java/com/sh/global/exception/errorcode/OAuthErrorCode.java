package com.sh.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum OAuthErrorCode implements ErrorCode {

    UNSUPPORTED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "OA_001", "지원하지 않는 OAuth Provider 입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    OAuthErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }
}
