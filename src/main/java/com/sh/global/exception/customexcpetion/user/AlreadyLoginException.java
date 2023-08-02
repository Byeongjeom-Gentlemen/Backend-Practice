package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.UserErrorCode;

public class AlreadyLoginException extends CustomException {

    public AlreadyLoginException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
