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

    FORBIDDEN_REQUEST_USER(HttpStatus.FORBIDDEN, "U_018", "해당 리소스에 접근할 권한이 없습니다.(로그인 필요)"),

    // 잘못된 인증(해당 아이디가 존재하지 않거나 비밀번호가 틀렸을 경우)
    INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "U_004", "사용자 인증에 실패하였습니다."),

    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED,"U_005", "인증 토큰이 존재하지 않습니다.(유효하지 않은 토큰)"),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "U_006","잘못된 토큰 정보입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "U_007","만료된 토큰 정보입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "U_008","지원하지 않는 토큰 방식입니다."),

    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "U_009","알 수 없는 이유로 요청이 거절되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
