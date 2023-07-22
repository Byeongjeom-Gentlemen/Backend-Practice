package com.sh.global.exception.customexcpetion.board;

import com.sh.global.exception.BoardErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class SearchKeywordIsEmptyException extends CustomException {

    public SearchKeywordIsEmptyException(BoardErrorCode errorCode) {
        super(errorCode);
    }
}
