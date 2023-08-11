package com.sh.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum PageErrorCode implements ErrorCode {

    // pageNumber & pageSize 가 숫자가 아닐 경우
    IS_NOT_INTEGER(HttpStatus.BAD_REQUEST, "P_001", "PageNumber & PageSize는 정수만 허용합니다."),

    // pageNumber & pageSize 가 음수인 경우
    NEGATIVE_VALUE(HttpStatus.BAD_REQUEST, "P_002", "PageNumber & PageSize는 음수 값을 허용하지 않습니다."),

    // pageSize 값이 max 값을 벗어난 경우
    SIZE_RANGE_OVER(HttpStatus.BAD_REQUEST, "P_003", "PageSize의 범위는 0 ~ 10 사이의 정수여야 합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    PageErrorCode(final HttpStatus status, final String code, final String message) {
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
