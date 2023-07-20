package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.BoardErrorCode;
import lombok.Getter;

@Getter
public class NotMatchesWriterException extends RuntimeException {

    private BoardErrorCode errorCode;

    public NotMatchesWriterException(BoardErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
