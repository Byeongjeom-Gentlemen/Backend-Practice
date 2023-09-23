package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.OAuthErrorCode;

public class OAuthCustomException extends CustomException {

    public static final OAuthCustomException UNSUPPORTED_OAUTH_PROVIDER =
            new OAuthCustomException(OAuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER);

    public OAuthCustomException(OAuthErrorCode errorCode) {
        super(errorCode);
    }
}
