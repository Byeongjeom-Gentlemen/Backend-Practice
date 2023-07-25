package com.sh.global.exception;

import com.sh.global.exception.errorcode.ErrorCode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
public class ErrorResponse {
    private String code;
    private String message;
    private HttpStatus status;
    private List<CustomFieldError> errors;

    @AllArgsConstructor
    @Getter
    static class CustomFieldError {
        private String field;
        private String invalidValue;
        private String reason;

        private CustomFieldError(FieldError fieldError) {
            this.field = fieldError.getField();
            this.invalidValue = fieldError.getRejectedValue().toString();
            this.reason = fieldError.getDefaultMessage();
        }
    }

    private void setErrorCode(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }

    private ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
        setErrorCode(errorCode);
        this.errors = errors.stream().map(CustomFieldError::new).collect(Collectors.toList());
    }

    private ErrorResponse(ErrorCode errorCode, String exceptionMessage) {
        setErrorCode(errorCode);
        this.errors = List.of(new CustomFieldError("", "", exceptionMessage));
    }

    // 정적 팩토리
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, Collections.emptyList());
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ErrorResponse(errorCode, bindingResult.getFieldErrors());
    }

    public static ErrorResponse of(ErrorCode errorCode, String exceptionMessage) {
        return new ErrorResponse(errorCode, exceptionMessage);
    }
}
