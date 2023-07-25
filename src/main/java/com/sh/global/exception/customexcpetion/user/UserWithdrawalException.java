package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.UserErrorCode;

public class UserWithdrawalException extends CustomException {

    public UserWithdrawalException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
