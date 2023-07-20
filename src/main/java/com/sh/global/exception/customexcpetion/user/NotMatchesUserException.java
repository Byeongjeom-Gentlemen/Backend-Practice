package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class NotMatchesUserException extends RuntimeException {

    private UserErrorCode errorCode;

    public NotMatchesUserException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
