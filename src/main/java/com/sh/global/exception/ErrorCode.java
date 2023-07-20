package com.sh.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getCode();

    String getMessage();

    HttpStatus getStatus();
}
