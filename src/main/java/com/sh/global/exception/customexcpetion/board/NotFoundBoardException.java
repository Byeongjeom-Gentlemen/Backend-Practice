package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.errorcode.BoardErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class NotFoundBoardException extends CustomException {

    public NotFoundBoardException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
