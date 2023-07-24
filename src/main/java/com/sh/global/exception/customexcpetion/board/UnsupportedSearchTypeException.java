package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.errorcode.BoardErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;
import lombok.Setter;

@Setter
public class UnsupportedSearchTypeException extends CustomException {

    public UnsupportedSearchTypeException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
