package com.sh.global.exception.customexcpetion.token;

import com.sh.global.exception.customexcpetion.CustomException;
import com.sh.global.exception.errorcode.TokenErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;

public class UnauthorizedTokenException extends CustomException {

    public UnauthorizedTokenException(TokenErrorCode errorCode) {
        super(errorCode);
    }

}