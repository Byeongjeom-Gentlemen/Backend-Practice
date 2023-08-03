package com.sh.global.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TokenErrorCode implements ErrorCode {

    /* JWT */
    NON_TOKEN(HttpStatus.FORBIDDEN, "T_001", "토큰 정보가 존재하지 않습니다."),
    WRONG_TYPE_SIGNATURE(HttpStatus.FORBIDDEN, "T_002", "잘못된 JWT 시그니처입니다."),
    WRONG_TYPE_TOKEN(
            HttpStatus.FORBIDDEN, "T_003", "지원되지 않는 형식이나 구성의 토큰입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.FORBIDDEN, "T_004", "만료된 토큰으로 유효하지 않은 토큰 정보입니다."),
    MALFORMED_ACCESS_TOKEN(HttpStatus.FORBIDDEN, "T_005", "손상된 토큰 정보입니다."),
    NON_ACCESS_TOKEN_REQUEST_HEADER(HttpStatus.UNAUTHORIZED, "T_006", "Request Header에 Access Token 정보가 존재하지 않습니다."),
    NON_REFRESH_TOKEN_REQUEST_HEADER(HttpStatus.UNAUTHORIZED, "T_007", "Request Header에 Refresh Token 정보가 존재하지 않습니다."),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "T_008", "해당 토큰 정보를 찾을 수 없습니다."),
    UNAVAILABLE_TOKENS(HttpStatus.FORBIDDEN, "T_009", "사용할 수 없는 토큰입니다. 재로그인이 필요합니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "T_010", "만료된 Refresh Token 입니다. 재로그인이 필요합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    TokenErrorCode(final HttpStatus status, final String code, final String message) {
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
