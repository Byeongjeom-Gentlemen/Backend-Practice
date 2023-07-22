package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.BoardErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class NotMatchesWriterException extends CustomException {

    public NotMatchesWriterException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
