package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.CommonErrorCode;

public class CommonCustomException extends CustomException {

    public static final CommonCustomException TRY_AGAIN_LATER =
            new CommonCustomException(CommonErrorCode.TRY_AGAIN_LATER);

    public CommonCustomException(CommonErrorCode errorCode) {
        super(errorCode);
    }
}
