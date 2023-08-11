package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.TokenErrorCode;

public class TokenCustomException extends CustomException {

    // Request Header Access Token 이 없을 경우
    public static final TokenCustomException NON_ACCESS_TOKEN_REQUEST_HEADER = new TokenCustomException(TokenErrorCode.NON_ACCESS_TOKEN_REQUEST_HEADER);
    
    // Request Header Refresh Token 이 없을 경우
    public static final TokenCustomException NON_REFRESH_TOKEN_REQUEST_HEADER = new TokenCustomException(TokenErrorCode.NON_REFRESH_TOKEN_REQUEST_HEADER);

    // 사용할 수 없는 토큰일 경우(블랙리스트 토큰)
    public static final TokenCustomException UNAVAILABLE_TOKENS = new TokenCustomException(TokenErrorCode.UNAVAILABLE_TOKENS);

    // 만료된 Refresh Token 일 경우
    public static final TokenCustomException EXPIRED_REFRESH_TOKEN = new TokenCustomException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);

    public TokenCustomException(TokenErrorCode errorCode) {
        super(errorCode);
    }
}
