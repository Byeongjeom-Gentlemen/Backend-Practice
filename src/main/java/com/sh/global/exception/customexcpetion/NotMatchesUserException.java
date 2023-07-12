package com.sh.global.exception.customexcpetion;

public class NotMatchesUserException extends RuntimeException {

    public NotMatchesUserException(String message) {
        super(message);
    }
}
