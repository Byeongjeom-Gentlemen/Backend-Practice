package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.PageErrorCode;

public class PageCustomException extends CustomException {

    // 숫자가 아닌 경우
    public static final PageCustomException IS_NOT_INTEGER = new PageCustomException(PageErrorCode.IS_NOT_INTEGER);

    // 음수일 경우
    public static final PageCustomException NEGATIVE_NUMBER = new PageCustomException(PageErrorCode.NEGATIVE_VALUE);

    // 페이지 사이즈가 범위를 벗어난 경우
    public static final PageCustomException RANGE_OVER = new PageCustomException(PageErrorCode.SIZE_RANGE_OVER);

    public PageCustomException(PageErrorCode errorCode) {
        super(errorCode);
    }
}
