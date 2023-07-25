package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.BoardErrorCode;

public class NotFoundBoardException extends CustomException {

    public NotFoundBoardException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
