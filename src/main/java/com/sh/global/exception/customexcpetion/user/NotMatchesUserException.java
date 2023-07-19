package com.sh.global.exception.customexcpetion.user;

import com.sh.global.exception.UserErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class NotMatchesUserException extends RuntimeException {

    private UserErrorCode errorCode;

    public NotMatchesUserException(UserErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
