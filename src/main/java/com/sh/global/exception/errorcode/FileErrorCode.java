package com.sh.global.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FileErrorCode implements ErrorCode {

    IS_NOT_IMAGE(HttpStatus.BAD_REQUEST, "F_001", "이미지 파일만 업로드 가능합니다."),
    FILE_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "F_002", "파일정보가 존재하지 않습니다."),
    FILE_DOES_NOT_EXIST_REQUEST_VALUE(HttpStatus.BAD_REQUEST, "F_003", "요청 값에 파일정보가 존재하지 않습니다."),
    FAILED_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "F_004", "파일 업로드에 실패하였습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    FileErrorCode(final HttpStatus status, final String code, final String message) {
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
