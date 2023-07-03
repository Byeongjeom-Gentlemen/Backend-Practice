package com.sh.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// Response 형태 커스텀
public class ApiResponse<T> {

    private int state;
    private String result;
    private String message;
    private T data;
    private Error error;

    // 성공 시
    public <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, "success", message, data, null);
    }

    // 실패 시
    public  <T> ApiResponse<T> fail(T data, String message, String errorCode, String errorMessage) {
        return new ApiResponse<>(Integer.parseInt(errorCode), "fail", message, data, new Error(errorCode, errorMessage));
    }

    public <T> ApiResponse<T> errors(String errorCode, String errorMessage) {
        return new ApiResponse<>(Integer.parseInt(errorCode), "error", "", null, new Error(errorCode, errorMessage));
    }


    @Getter
    @AllArgsConstructor
    static class Error {
        private String errorCode;
        private String errorMessage;
    }
}
