package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

public class AlreadyUsedUserNicknameException extends CustomException {

    public AlreadyUsedUserNicknameException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
