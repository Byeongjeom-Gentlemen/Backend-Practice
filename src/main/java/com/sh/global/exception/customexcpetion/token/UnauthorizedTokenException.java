package com.sh.global.exception.customexcpetion.token;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.TokenErrorCode;

public class UnauthorizedTokenException extends CustomException {

    public UnauthorizedTokenException(TokenErrorCode errorCode) {
        super(errorCode);
    }
}
