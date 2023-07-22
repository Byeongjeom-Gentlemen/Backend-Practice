package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
