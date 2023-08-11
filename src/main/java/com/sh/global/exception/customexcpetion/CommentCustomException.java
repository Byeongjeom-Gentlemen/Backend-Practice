package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.CommentErrorCode;

public class CommentCustomException extends CustomException {

    // 해당 댓글이 존재하지 않을 경우
    public static final CommentCustomException COMMENT_NOT_FOUND = new CommentCustomException(CommentErrorCode.NOT_FOUND_COMMENT);

    // 삭제된 댓글인 경우
    public static final CommentCustomException DELETED_COMMENT = new CommentCustomException(CommentErrorCode.DELETED_COMMENT);

    // 댓글 관련 작업에 권한이 없을 경우
    public static final CommentCustomException NOT_AUTHORITY_COMMENT = new CommentCustomException(CommentErrorCode.NOT_AUTHORITY_COMMENT);

    public CommentCustomException(CommentErrorCode errorCode) {
        super(errorCode);
    }
}
