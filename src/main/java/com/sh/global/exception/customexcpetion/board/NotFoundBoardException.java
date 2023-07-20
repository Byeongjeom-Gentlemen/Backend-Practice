package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.BoardErrorCode;
import com.sh.global.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class NotFoundBoardException extends RuntimeException {

    private BoardErrorCode errorCode;

    public NotFoundBoardException(BoardErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
