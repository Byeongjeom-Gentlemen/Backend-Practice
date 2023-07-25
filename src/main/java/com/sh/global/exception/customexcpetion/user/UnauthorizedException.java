package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.UserErrorCode;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
