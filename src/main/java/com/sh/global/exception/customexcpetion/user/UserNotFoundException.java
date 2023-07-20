package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private UserErrorCode errorCode;

    public UserNotFoundException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
