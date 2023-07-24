package com.sh.global.exception.customexcpetion.comment;

import com.sh.global.exception.errorcode.CommentErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class NotFoundCommentException extends CustomException {

    public NotFoundCommentException(CommentErrorCode errorCode) {
        super((errorCode));
    }
}
