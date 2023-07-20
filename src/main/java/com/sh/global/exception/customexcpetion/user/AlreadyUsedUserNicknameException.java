package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class AlreadyUsedUserNicknameException extends RuntimeException {

    private UserErrorCode errorCode;

    public AlreadyUsedUserNicknameException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
