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

    // 해당 유저가 없을 경우
    NOT_FOUND_USER(HttpStatus.UNAUTHORIZED, "U_020", "해당 사용자가 존재하지 않습니다."),
    // 탈퇴한 회원인 경우
    WITHDRAWAL_USER(HttpStatus.UNAUTHORIZED, "U_010", "이미 탈퇴한 회원입니다"),

    // 잘못된 인증(해당 아이디가 존재하지 않거나 비밀번호가 틀렸을 경우)
    INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "U_004", "사용자 인증에 실패하였습니다."),

    // 비회원일 경우
    NON_LOGIN(HttpStatus.UNAUTHORIZED, "U_011", "로그인이 필요한 서비스 입니다."),

    // JWT
    NON_TOKEN(HttpStatus.UNAUTHORIZED, "U_008", "토큰정보가 존재하지 않습니다."),
    WRONG_TYPE_SIGNATURE(HttpStatus.UNAUTHORIZED, "U_005", "잘못된 JWT 서명입니다."),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "U_006", "유효하지 않은 구성의 토큰입니다.(지원되지 않는 형식이나 구성의 토큰입니다.)"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "U_007", "만료된 토큰 정보입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "U_009", "잘못된 토큰 정보입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
