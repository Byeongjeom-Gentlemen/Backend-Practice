package com.sh.global.exception.customexcpetion.comment;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.CommentErrorCode;

public class NotFoundCommentException extends CustomException {

    public NotFoundCommentException(CommentErrorCode errorCode) {
        super((errorCode));
    }
}
