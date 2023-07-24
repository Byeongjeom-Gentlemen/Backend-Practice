package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.errorcode.BoardErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class BoardListIsEmptyException extends CustomException {

    public BoardListIsEmptyException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
