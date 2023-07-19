package com.sh.global.exception;

import org.springframework.http.HttpStatus;

public enum BoardErrorCode{

    /* BOARD */
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "B_001", "해당 게시글이 존재하지 않습니다"),
    BOARD_NOT_AUTHORITY(HttpStatus.UNAUTHORIZED, "B_002", "해당 게시글에 대한 권한이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    BoardErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
