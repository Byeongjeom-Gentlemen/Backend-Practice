package com.sh.global.exception.customexcpetion;

public class AlreadyUsedUserIdException extends RuntimeException {

    public AlreadyUsedUserIdException(String message) {
        super(message);
    }
}
