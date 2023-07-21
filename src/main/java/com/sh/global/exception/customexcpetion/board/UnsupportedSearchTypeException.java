package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.BoardErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;
import lombok.Getter;
import lombok.Setter;

@Setter
public class UnsupportedSearchTypeException extends CustomException {

    public UnsupportedSearchTypeException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
