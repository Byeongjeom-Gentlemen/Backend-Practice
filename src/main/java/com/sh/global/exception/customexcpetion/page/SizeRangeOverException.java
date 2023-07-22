package com.sh.global.exception.customexcpetion.page;

import com.sh.global.exception.PageErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class SizeRangeOverException extends CustomException {

    public SizeRangeOverException(PageErrorCode errorCode) {
        super(errorCode);
    }
}
