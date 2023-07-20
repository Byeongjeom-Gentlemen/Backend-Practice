package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class UserNonLoginException extends RuntimeException {

    private UserErrorCode errorCode;

    public UserNonLoginException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
