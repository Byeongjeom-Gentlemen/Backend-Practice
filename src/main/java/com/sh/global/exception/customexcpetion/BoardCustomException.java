package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.BoardErrorCode;

public class BoardCustomException extends CustomException {

    // 게시글이 존재하지 않을 경우
    public static final BoardCustomException BOARD_NOT_FOUND =
            new BoardCustomException(BoardErrorCode.NOT_FOUND_BOARD);

    // 삭제된 게시글일 경우
    public static final BoardCustomException DELETED_BOARD =
            new BoardCustomException(BoardErrorCode.DELETED_BOARD);

    // 로그인한 회원과 게시글의 작성자가 다른 경우
    public static final BoardCustomException NOT_MATCHES_WRITER =
            new BoardCustomException(BoardErrorCode.BOARD_NOT_AUTHORITY);

    // 검색 키워드가 비워있을 경우
    public static final BoardCustomException SEARCH_KEYWORD_IS_EMPTY =
            new BoardCustomException(BoardErrorCode.KEYWORD_EMPTY);

    // 지원하지 않는 검색 종류일 경우
    public static final BoardCustomException UNSUPPORTED_SEARCH_TYPE =
            new BoardCustomException(BoardErrorCode.UNSUPPORTED_SEARCH_TYPE);

    // 좋아요 등록 시 게시글의 좋아요가 이미 눌러져 있을 경우
    public static final BoardCustomException ALREADY_PRESSED_LIKE =
            new BoardCustomException(BoardErrorCode.ALREADY_PRESSED_LIKE);

    // 좋아요 취소 시 게시글의 좋아요 기록이 존재하지 않을 경우
    public static final BoardCustomException NOT_FOUND_LIKE =
            new BoardCustomException(BoardErrorCode.NOT_FOUND_LIKE);

    public BoardCustomException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
