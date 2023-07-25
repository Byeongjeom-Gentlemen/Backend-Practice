package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.UserErrorCode;

public class AlreadyUsedUserNicknameException extends CustomException {

    public AlreadyUsedUserNicknameException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
