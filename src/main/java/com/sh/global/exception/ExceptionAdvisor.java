package com.sh.global.exception;

import com.sh.global.common.response.BasicResponse;
import com.sh.global.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

// 예외발생 시 처리해주는 객체
@ControllerAdvice
@RestController
public class ExceptionAdvisor {

    // @Valid를 통한 유효성 검사에서 실패했을 시 수행하는 로직
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<? extends BasicResponse> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder sb = new StringBuilder();
        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append("[")
                    .append(fieldError.getField())
                    .append("\"(은)는 ")
                    .append(fieldError.getDefaultMessage())
                    .append(" 입력된 값 : [")
                    .append(fieldError.getRejectedValue())
                    .append("]")
                    .append("\n");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(sb.toString(), "400"));
    }
}
