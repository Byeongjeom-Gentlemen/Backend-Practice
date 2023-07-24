package com.sh.global.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentErrorCode implements ErrorCode {

    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "C_001", "해당 댓글이 존재하지 않습니다."),
    NOT_AUTHORITY_COMMENT(HttpStatus.UNAUTHORIZED, "C_002", "해당 댓글에 대한 작업권한이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    CommentErrorCode(final HttpStatus status, final String code, final String message) {
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
