package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class UserWithdrawalException extends RuntimeException {

    private UserErrorCode errorCode;

    public UserWithdrawalException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
