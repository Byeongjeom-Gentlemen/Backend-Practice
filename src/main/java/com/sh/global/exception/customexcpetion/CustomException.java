package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) { this.errorCode = errorCode; }
}