package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.UserErrorCode;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
