package com.sh.global.exception.customexcpetion.page;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.PageErrorCode;

public class PageRangeOverException extends CustomException {

    public PageRangeOverException(PageErrorCode errorCode) {
        super(errorCode);
    }
}
