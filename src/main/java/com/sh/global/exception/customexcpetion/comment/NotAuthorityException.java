package com.sh.global.exception.customexcpetion.comment;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.CommentErrorCode;

public class NotAuthorityException extends CustomException {

    public NotAuthorityException(CommentErrorCode errorCode) {
        super(errorCode);
    }
}
