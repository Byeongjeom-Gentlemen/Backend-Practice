package com.sh.global.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends BasicResponse {

    private String data;
    private String errorMessage;
    private String errorCode;

    /* public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorCode = "404";
    }*/

    public ErrorResponse(String data, String errorMessage, String errorCode) {
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ErrorResponse(String errorMessage, String errorCode) {
        this.data = "";
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

}
