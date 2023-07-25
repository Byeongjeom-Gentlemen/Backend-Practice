package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.BoardErrorCode;

public class NotFoundLikeException extends CustomException {

    public NotFoundLikeException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
