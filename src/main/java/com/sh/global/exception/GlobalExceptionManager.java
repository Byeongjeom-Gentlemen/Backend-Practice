package com.sh.global.exception;

import com.sh.global.exception.customexcpetion.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 예외발생 시 처리해주는 객체
@RestControllerAdvice
public class GlobalExceptionManager {

    // @Valid를 통한 유효성 검사에서 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_VALUE, e.getBindingResult());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(AlreadyUsedUserIdException.class)
    public ResponseEntity<ErrorResponse> existsByIdError(AlreadyUsedUserIdException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ALREADY_EXISTS_ID, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }


    @ExceptionHandler(AlreadyUsedUserNicknameException.class)
    public ResponseEntity<ErrorResponse> existsByNicknameError(AlreadyUsedUserNicknameException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ALREADY_EXISTS_NICKNAME, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialError(BadCredentialsException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_AUTHENTICATION);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundError(UserNotFoundException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(NotMatchesUserException.class)
    public ResponseEntity<ErrorResponse> notMatchesError(NotMatchesUserException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_AUTHENTICATION);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }


    /*@ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> unAuthorizationError(UnauthorizedException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UNAUTHORIZED, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }*/

}
