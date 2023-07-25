package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.UserErrorCode;

public class UserNonLoginException extends CustomException {

    public UserNonLoginException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
