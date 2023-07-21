package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;
import lombok.Getter;

public class UserNonLoginException extends CustomException {

    public UserNonLoginException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
