package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.CommonErrorCode;

public class CommonCustomException extends CustomException {

    public static final CommonCustomException TRY_AGAIN_LATER =
            new CommonCustomException(CommonErrorCode.TRY_AGAIN_LATER);

    public static final CommonCustomException NON_NUMBER =
            new CommonCustomException(CommonErrorCode.NON_NUMBER);

    public CommonCustomException(CommonErrorCode errorCode) {
        super(errorCode);
    }
}
