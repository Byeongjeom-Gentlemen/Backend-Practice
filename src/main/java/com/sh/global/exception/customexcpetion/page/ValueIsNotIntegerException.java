package com.sh.global.exception.customexcpetion.page;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.PageErrorCode;

public class ValueIsNotIntegerException extends CustomException {

    public ValueIsNotIntegerException(PageErrorCode errorCode) {
        super(errorCode);
    }
}
