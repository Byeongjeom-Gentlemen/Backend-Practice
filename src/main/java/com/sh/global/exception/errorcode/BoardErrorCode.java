package com.sh.global.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BoardErrorCode implements ErrorCode {

    /* BOARD */
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "B_001", "해당 게시글이 존재하지 않습니다"),
    EMPTY_BOARD_LIST(HttpStatus.NOT_FOUND, "B_002", "등록된 게시글이 없습니다."),
    BOARD_NOT_AUTHORITY(HttpStatus.UNAUTHORIZED, "B_003", "해당 게시글에 대한 작업 권한이 존재하지 않습니다."),
    UNSUPPORTED_SEARCH_TYPE(HttpStatus.BAD_REQUEST, "B_004", "해당 타입의 검색은 지원하지 않습니다."),
    NOT_FOUND_SEARCH_TITLE(HttpStatus.NOT_FOUND, "B_005", "해당 제목을 포함한 게시글이 존재하지 않습니다."),
    NOT_FOUND_SEARCH_WRITER(HttpStatus.NOT_FOUND, "B_006", "해당 작성자의 게시글이 존재하지 않습니다."),
    KEYWORD_EMPTY(HttpStatus.BAD_REQUEST, "B_007", "검색어를 입력해주세요."),
    DELETED_BOARD(HttpStatus.UNAUTHORIZED, "b_008", "삭제된 게시글입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    BoardErrorCode(final HttpStatus status, final String code, final String message) {
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
