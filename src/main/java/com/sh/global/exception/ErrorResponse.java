package com.sh.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    private void setErrorCode(UserErrorCode userErrorCode) {
        this.code = userErrorCode.getCode();
        this.message = userErrorCode.getMessage();
        this.status = userErrorCode.getStatus();
    }

    private ErrorResponse(UserErrorCode userErrorCode, List<FieldError> errors) {
        setErrorCode(userErrorCode);
        this.errors = errors.stream().map(CustomFieldError::new).collect(Collectors.toList());
    }

    private ErrorResponse(UserErrorCode userErrorCode, String exceptionMessage) {
        setErrorCode(userErrorCode);
        this.errors = List.of(new CustomFieldError("", "", exceptionMessage));
    }

    // 정적 팩토리
    public static ErrorResponse from(UserErrorCode userErrorCode) {
        return new ErrorResponse(userErrorCode, Collections.emptyList());
    }

    public static ErrorResponse of(UserErrorCode userErrorCode, BindingResult bindingResult) {
        return new ErrorResponse(userErrorCode, bindingResult.getFieldErrors());
    }

    public static ErrorResponse of(UserErrorCode userErrorCode, String exceptionMessage){
        return new ErrorResponse(userErrorCode, exceptionMessage);
    }
}
