package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.BoardErrorCode;

public class NotMatchesWriterException extends CustomException {

    public NotMatchesWriterException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
