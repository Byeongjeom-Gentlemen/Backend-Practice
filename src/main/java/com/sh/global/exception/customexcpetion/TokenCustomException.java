package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.TokenErrorCode;

public class TokenCustomException extends CustomException {

    // Request Header Access Token 이 없을 경우
    public static final TokenCustomException NON_ACCESS_TOKEN_REQUEST_HEADER =
            new TokenCustomException(TokenErrorCode.NON_ACCESS_TOKEN_REQUEST_HEADER);

    // Request Header Refresh Token 이 없을 경우
    public static final TokenCustomException NON_REFRESH_TOKEN_REQUEST_HEADER =
            new TokenCustomException(TokenErrorCode.NON_REFRESH_TOKEN_REQUEST_HEADER);

    // 사용할 수 없는 토큰일 경우(블랙리스트 토큰)
    public static final TokenCustomException UNAVAILABLE_TOKENS =
            new TokenCustomException(TokenErrorCode.UNAVAILABLE_TOKENS);

    // 만료된 Token 일 경우
    public static final TokenCustomException EXPIRED_TOKEN =
            new TokenCustomException(TokenErrorCode.EXPIRED_TOKEN);

    // 만료된 Refresh Token 으로 재로그인이 필요한 경우
    public static final TokenCustomException EXPIRED_REFRESH_TOKEN =
            new TokenCustomException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);

    // 토큰이 없을 경우
    public static final TokenCustomException NON_TOKEN =
            new TokenCustomException(TokenErrorCode.NON_TOKEN);

    // 잘못된 시그니처 인증일 경우
    public static final TokenCustomException WRONG_TYPE_SIGNATURE =
            new TokenCustomException(TokenErrorCode.WRONG_TYPE_SIGNATURE);

    // 지원하지 않는 형식의 토큰일 경우
    public static final TokenCustomException WRONG_TYPE_TOKEN =
            new TokenCustomException(TokenErrorCode.WRONG_TYPE_TOKEN);

    // 손상된 토큰일 경우
    public static final TokenCustomException MALFORMED_TOKEN =
            new TokenCustomException(TokenErrorCode.MALFORMED_TOKEN);

    // 재발급이 불가한 경우
    public static final TokenCustomException UNABLE_TOKEN_REISSUE =
            new TokenCustomException(TokenErrorCode.UNABLE_TOKEN_REISSUE);

    // 토큰 정보를 찾을 수 없을 경우
    public static final TokenCustomException NOT_FOUND_TOKEN =
            new TokenCustomException(TokenErrorCode.NOT_FOUND_TOKEN);

    public TokenCustomException(TokenErrorCode errorCode) {
        super(errorCode);
    }
}
