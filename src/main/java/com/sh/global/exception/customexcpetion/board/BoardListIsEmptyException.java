package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.BoardErrorCode;

public class BoardListIsEmptyException extends CustomException {

    public BoardListIsEmptyException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
