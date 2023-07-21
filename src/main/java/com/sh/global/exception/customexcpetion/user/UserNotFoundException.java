package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;
import lombok.Getter;

public class UserNotFoundException extends CustomException {

    public UserNotFoundException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
