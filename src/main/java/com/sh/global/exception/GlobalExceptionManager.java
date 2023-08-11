package com.sh.global.exception;

import com.sh.global.exception.customexcpetion.*;
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

    // User Custom Exception
    @ExceptionHandler(UserCustomException.class)
    public ResponseEntity<ErrorResponse> userError(UserCustomException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }

    // Board Custom Exception
    @ExceptionHandler(BoardCustomException.class)
    public ResponseEntity<ErrorResponse> boardError(BoardCustomException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }

    // Comment Custom Exception
    @ExceptionHandler(CommentCustomException.class)
    public ResponseEntity<ErrorResponse> commentError(CommentCustomException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }

    // Token Custom Exception
    @ExceptionHandler(TokenCustomException.class)
    public ResponseEntity<ErrorResponse> tokenError(TokenCustomException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }

    // Page Custom Exception
    @ExceptionHandler(PageCustomException.class)
    public ResponseEntity<ErrorResponse> pageError(PageCustomException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }
}
