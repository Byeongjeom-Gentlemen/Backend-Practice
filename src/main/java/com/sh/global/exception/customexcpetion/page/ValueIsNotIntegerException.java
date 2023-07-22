package com.sh.global.exception.customexcpetion.page;

import com.sh.global.exception.PageErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class ValueIsNotIntegerException extends CustomException {

    public ValueIsNotIntegerException(PageErrorCode errorCode) {
        super(errorCode);
    }
}
