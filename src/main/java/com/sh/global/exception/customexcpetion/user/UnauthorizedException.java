package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    private UserErrorCode errorCode;
    public UnauthorizedException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
