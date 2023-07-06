package com.sh.global.exception;

import com.sh.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

// 예외발생 시 처리해주는 객체
@RestControllerAdvice
public class ExceptionManager {

    // RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> runtimeExceptionHandler(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>().errors("500", e.getMessage()));
    }


    // @Valid를 통한 유효성 검사에서 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        List<ApiResponse> errorList = new ArrayList<>();
        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            errorList.add(new ApiResponse().fail(fieldError.getField(), fieldError.getDefaultMessage(), "400", "BAD_REQUEST"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorList);
    }

    /*@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> illegalArgumentError(IllegalArgumentException exception) {
        return ResponseEntity
    }*/

    // 로그인 비밀번호 불일치
    /*@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> passwordMatchesError(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse().fail(null, exception.getMessage(), "401", "Unauthorized"));
    }*/
}
