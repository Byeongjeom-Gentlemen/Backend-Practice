package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class NotMatchesUserException extends CustomException {

    public NotMatchesUserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
