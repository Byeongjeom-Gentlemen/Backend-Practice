package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.UserErrorCode;

public class UnauthorizedTokenException extends CustomException {

    public UnauthorizedTokenException(UserErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedTokenException(String message) {
        super(message);
    }

}
