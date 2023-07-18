package com.sh.global.exception;

import com.sh.global.exception.customexcpetion.board.NotFoundBoardException;
import com.sh.global.exception.customexcpetion.board.NotMatchesWriterException;
import com.sh.global.exception.customexcpetion.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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


    // 아이디 중복 예외
    @ExceptionHandler(AlreadyUsedUserIdException.class)
    public ResponseEntity<ErrorResponse> existsByIdError(AlreadyUsedUserIdException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ALREADY_EXISTS_ID, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }


    // 닉네임 중복 예외
    @ExceptionHandler(AlreadyUsedUserNicknameException.class)
    public ResponseEntity<ErrorResponse> existsByNicknameError(AlreadyUsedUserNicknameException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ALREADY_EXISTS_NICKNAME, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(response);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialError(BadCredentialsException e) {
        final ErrorResponse response = ErrorResponse.from(ErrorCode.INVALID_AUTHENTICATION);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // 해당 유저를 찾을 수 없을 때 예외
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundError(UserNotFoundException e) {
        final ErrorResponse response = ErrorResponse.from(ErrorCode.NOT_FOUND_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // 유저의 정보가 일치하지 않을 때 예외
    @ExceptionHandler(NotMatchesUserException.class)
    public ResponseEntity<ErrorResponse> notMatchesError(NotMatchesUserException e) {
        final ErrorResponse response = ErrorResponse.from(ErrorCode.INVALID_AUTHENTICATION);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // 로그인이 되어있지 않을 때 예외
    @ExceptionHandler(UserNonLoginException.class)
    public ResponseEntity<ErrorResponse> nonLoginError(UserNonLoginException e) {
        final ErrorResponse response = ErrorResponse.from(ErrorCode.NON_LOGIN);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // 해당 게시글을 찾을 수 없을 때 예외
    @ExceptionHandler(NotFoundBoardException.class)
    public ResponseEntity<ErrorResponse> notFoundBoardError(NotFoundBoardException e) {
        final ErrorResponse response = ErrorResponse.from(ErrorCode.NOT_FOUND_BOARD);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // 게시글의 작성자 정보가 틀릴 경우 예외
    @ExceptionHandler(NotMatchesWriterException.class)
    public ResponseEntity<ErrorResponse> notMatchesWriterError(NotMatchesWriterException e) {
        final ErrorResponse response = ErrorResponse.from(ErrorCode.BOARD_NOT_AUTHORITY);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
}
