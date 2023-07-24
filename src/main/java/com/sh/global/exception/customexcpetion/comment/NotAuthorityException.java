package com.sh.global.exception.customexcpetion.comment;

import com.sh.global.exception.errorcode.CommentErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class NotAuthorityException extends CustomException {

    public NotAuthorityException(CommentErrorCode errorCode) {
        super(errorCode);
    }
}
