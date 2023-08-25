package com.sh.global.exception;

import com.sh.global.exception.customexcpetion.*;
import com.sh.global.exception.errorcode.CommonErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 예외발생 시 처리해주는 객체
@RestControllerAdvice
public class GlobalExceptionManager {

    // @Valid를 통한 유효성 검사에서 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException e) {
        final ErrorResponse response =
                ErrorResponse.of(UserErrorCode.INVALID_VALUE, e.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponse> nonNumberError(NumberFormatException e) {
        final ErrorResponse response = ErrorResponse.from(CommonErrorCode.NON_NUMBER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 타입 불일치
    /*
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> typeMismatchError(MethodArgumentTypeMismatchException e) {
    }
     */

    @ExceptionHandler({
        UserCustomException.class,
        BoardCustomException.class,
        CommentCustomException.class,
        TokenCustomException.class,
        PageCustomException.class,
        CommentCustomException.class
    })
    public ResponseEntity<ErrorResponse> customErrors(CustomException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }
}
