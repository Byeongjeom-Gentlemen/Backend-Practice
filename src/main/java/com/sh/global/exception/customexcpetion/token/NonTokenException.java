package com.sh.global.exception.customexcpetion.token;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.TokenErrorCode;

public class NonTokenException extends CustomException {

    public NonTokenException(TokenErrorCode errorCode) {
        super(errorCode);
    }
}
