package com.sh.global.exception.customexcpetion.page;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.PageErrorCode;

public class SizeRangeOverException extends CustomException {

    public SizeRangeOverException(PageErrorCode errorCode) {
        super(errorCode);
    }
}
