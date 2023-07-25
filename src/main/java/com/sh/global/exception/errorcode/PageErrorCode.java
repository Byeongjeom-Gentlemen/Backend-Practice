package com.sh.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum PageErrorCode implements ErrorCode {
    PAGE_VALUE_OVER_RANGE(HttpStatus.BAD_REQUEST, "P_001", "조회할 페이지는 양수여야 합니다."),
    SIZE_VALUE_OVER_RANGE(HttpStatus.BAD_REQUEST, "P_002", "한 페이지의 데이터는 최소 1개에서 최대 10개만을 허용합니다."),
    IS_NOT_INTEGER(HttpStatus.BAD_REQUEST, "P_003", "페이지와 페이지 크기는 정수만 허용합니다.");

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
