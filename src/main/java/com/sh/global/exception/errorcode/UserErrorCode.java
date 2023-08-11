package com.sh.global.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {
    /* USER */
    // 데이터 누락 및 잘못된 데이터 형식(아이디, 비밀번호, 닉네임)
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "U_001", "잘못된 형식의 데이터입니다."),

    // 데이터 중복(아이디, 닉네임)
    ALREADY_EXISTS_ID(HttpStatus.CONFLICT, "U_002", "이미 사용중인 아이디입니다."),
    ALREADY_EXISTS_NICKNAME(HttpStatus.CONFLICT, "U_003", "이미 사용중인 닉네임입니다."),
    FORBIDDEN_REQUEST_USER(HttpStatus.FORBIDDEN, "U_004", "해당 리소스에 접근할 권한이 없습니다.(로그인 필요)"),

    // 해당 유저가 없을 경우
    NOT_FOUND_USER(HttpStatus.UNAUTHORIZED, "U_005", "해당 사용자가 존재하지 않습니다."),

    // 잘못된 인증(해당 아이디가 존재하지 않거나 비밀번호가 틀렸을 경우, 로그인)
    INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "U_006", "사용자 인증에 실패하였습니다."),

    // 비회원일 경우
    NON_LOGIN(HttpStatus.UNAUTHORIZED, "U_007", "로그인이 필요한 서비스 입니다."),

    // 탈퇴한 회원인 경우
    WITHDRAWN_USER(HttpStatus.UNAUTHORIZED, "U_008", "탈퇴한 회원입니다."),
    ALREADY_LOGIN(HttpStatus.UNAUTHORIZED, "U_009", "이미 로그인이 되어있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    UserErrorCode(final HttpStatus status, final String code, final String message) {
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
