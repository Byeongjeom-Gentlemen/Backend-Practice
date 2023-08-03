package com.sh.global.exception.customexcpetion.token;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.TokenErrorCode;

public class ExpiredTokenException extends CustomException {

    public ExpiredTokenException(TokenErrorCode errorCode) {
        super(errorCode);
    }
}
