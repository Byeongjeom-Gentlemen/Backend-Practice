package com.sh.global.exception.customexcpetion;

public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }
}
