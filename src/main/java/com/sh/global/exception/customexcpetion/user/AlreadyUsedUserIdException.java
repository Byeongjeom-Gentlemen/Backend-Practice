package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class AlreadyUsedUserIdException extends CustomException {

    public AlreadyUsedUserIdException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
