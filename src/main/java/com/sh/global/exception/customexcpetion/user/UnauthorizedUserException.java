package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.UserErrorCode;

public class UnauthorizedUserException extends CustomException {

    public UnauthorizedUserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
