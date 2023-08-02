package com.sh.global.exception;

import com.sh.global.exception.customexcpetion.board.*;
import com.sh.global.exception.customexcpetion.comment.NotAuthorityException;
import com.sh.global.exception.customexcpetion.comment.NotFoundCommentException;
import com.sh.global.exception.customexcpetion.page.PageRangeOverException;
import com.sh.global.exception.customexcpetion.page.SizeRangeOverException;
import com.sh.global.exception.customexcpetion.page.ValueIsNotIntegerException;
import com.sh.global.exception.customexcpetion.token.NonTokenException;
import com.sh.global.exception.customexcpetion.user.*;
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

    // 아이디 중복 예외
    @ExceptionHandler(AlreadyUsedUserIdException.class)
    public ResponseEntity<ErrorResponse> existsByIdError(AlreadyUsedUserIdException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // 닉네임 중복 예외
    @ExceptionHandler(AlreadyUsedUserNicknameException.class)
    public ResponseEntity<ErrorResponse> existsByNicknameError(AlreadyUsedUserNicknameException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /*@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialError(BadCredentialsException e) {
    	final ErrorResponse response = ErrorResponse.from(UserErrorCode.INVALID_AUTHENTICATION);
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    			.body(response);
    }*/

    // 해당 유저를 찾을 수 없을 때 예외
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundError(UserNotFoundException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 유저의 정보가 일치하지 않을 때 예외
    @ExceptionHandler(NotMatchesUserException.class)
    public ResponseEntity<ErrorResponse> notMatchesError(NotMatchesUserException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 로그인이 되어있지 않을 때 예외
    @ExceptionHandler(UserNonLoginException.class)
    public ResponseEntity<ErrorResponse> nonLoginError(UserNonLoginException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 탈퇴한 회원인 경우
    @ExceptionHandler(UserWithdrawalException.class)
    public ResponseEntity<ErrorResponse> userWithdrawError(UserWithdrawalException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 토큰을 발급받지 않아 authentication이 없는 경우
    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ErrorResponse> unAuthorizedUserError(UnauthorizedUserException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Request Header에 필요 Token이 없는 경우
    @ExceptionHandler(NonTokenException.class)
    public ResponseEntity<ErrorResponse> nonTokenError(NonTokenException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 로그아웃을 하지 않고 이미 로그인되어 있는 상태로 재로그인을 요청할 경우
    @ExceptionHandler(AlreadyLoginException.class)
    public ResponseEntity<ErrorResponse> alreadyLoginError(AlreadyLoginException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 해당 게시글을 찾을 수 없을 때 예외
    @ExceptionHandler(NotFoundBoardException.class)
    public ResponseEntity<ErrorResponse> notFoundBoardError(NotFoundBoardException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 게시글의 작성자 정보가 틀릴 경우 예외
    @ExceptionHandler(NotMatchesWriterException.class)
    public ResponseEntity<ErrorResponse> notMatchesWriterError(NotMatchesWriterException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 지원하지 않는 검색종류일 경우(title, writer 외)
    @ExceptionHandler(UnsupportedSearchTypeException.class)
    public ResponseEntity<ErrorResponse> unsupportedSearchTypeError(
            UnsupportedSearchTypeException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 등록된 게시글이 없을 경우
    @ExceptionHandler(BoardListIsEmptyException.class)
    public ResponseEntity<ErrorResponse> boardListEmptyError(BoardListIsEmptyException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // 검색 keyword 값이 null 또는 빈값일 경우
    @ExceptionHandler(SearchKeywordIsEmptyException.class)
    public ResponseEntity<ErrorResponse> keywordEmptyError(SearchKeywordIsEmptyException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 페이지 범위 에러
    @ExceptionHandler(PageRangeOverException.class)
    public ResponseEntity<ErrorResponse> pageRangeOverError(PageRangeOverException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 사이즈 범위 에러
    @ExceptionHandler(SizeRangeOverException.class)
    public ResponseEntity<ErrorResponse> sizeRangeOverError(SizeRangeOverException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 페이지, 사이즈 값이 정수가 아닐 경우
    @ExceptionHandler(ValueIsNotIntegerException.class)
    public ResponseEntity<ErrorResponse> valueError(ValueIsNotIntegerException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 댓글을 찾을 수 없을 경우
    @ExceptionHandler(NotFoundCommentException.class)
    public ResponseEntity<ErrorResponse> notFoundCommentError(NotFoundCommentException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 댓글의 작성자가 로그인한 정보와 다를 경우
    @ExceptionHandler(NotAuthorityException.class)
    public ResponseEntity<ErrorResponse> notAuthorityError(NotAuthorityException e) {
        final ErrorResponse response = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
