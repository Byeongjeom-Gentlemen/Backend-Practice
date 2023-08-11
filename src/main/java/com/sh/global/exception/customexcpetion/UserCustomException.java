package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.UserErrorCode;

public class UserCustomException extends CustomException {
    // 회원을 찾을 수 없을 경우
    public static final UserCustomException USER_NOT_FOUND =
            new UserCustomException(UserErrorCode.NOT_FOUND_USER);

    // 탈퇴한 회원인 경우
    public static final UserCustomException WITHDRAWN_USER =
            new UserCustomException(UserErrorCode.WITHDRAWN_USER);

    // 아이디와 비밀번호가 틀린 경우(사용자 인증 실패)
    public static final UserCustomException NOT_MATCHED_USER =
            new UserCustomException(UserErrorCode.INVALID_AUTHENTICATION);

    // 이미 사용중인 닉네임인 경우
    public static final UserCustomException ALREADY_USED_USER_NICKNAME =
            new UserCustomException(UserErrorCode.ALREADY_EXISTS_NICKNAME);

    // 이미 사용중인 아이디인 경우
    public static final UserCustomException ALREADY_USED_USER_ID =
            new UserCustomException(UserErrorCode.ALREADY_EXISTS_ID);

    // 이미 로그인이 되어있는 경우
    public static final UserCustomException ALREADY_LOGIN =
            new UserCustomException(UserErrorCode.ALREADY_LOGIN);

    // 해당 요청에 접근할 수 없는 경우 (로그인이 필요한 서비스인 경우)
    public static final UserCustomException FORBIDDEN_REQUEST_USER =
            new UserCustomException(UserErrorCode.FORBIDDEN_REQUEST_USER);

    public UserCustomException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
