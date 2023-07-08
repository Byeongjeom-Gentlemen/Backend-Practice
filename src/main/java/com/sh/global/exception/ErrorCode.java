package com.sh.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Getter
public enum ErrorCode {
    // 데이터 누락 및 잘못된 데이터 형식(아이디, 비밀번호, 닉네임)
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "U_001", "Invalid Value."),

    // 데이터 중복(아이디, 닉네임)
    ALREADY_EXISTS_ID(HttpStatus.CONFLICT, "U_002", "Already Exists Id."),
    ALREADY_EXISTS_NICKNAME(HttpStatus.CONFLICT, "U_003", "Already Exists Nickname."),

    // 잘못된 인증(해당 아이디가 존재하지 않거나 비밀번호가 틀렸을 경우)
    INVALID_AUTHENTICATION(HttpStatus.NOT_FOUND, "U_004", "Invalid Authentication."),

    // 인증정보를 가지고 있는 사용자가 인증이 필요한 리소스 접근 시
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "U_005", "");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
