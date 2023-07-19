package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class AlreadyUsedUserIdException extends RuntimeException {

    private UserErrorCode errorCode;

    public AlreadyUsedUserIdException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
