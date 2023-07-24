package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class UserWithdrawalException extends CustomException {

    public UserWithdrawalException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
