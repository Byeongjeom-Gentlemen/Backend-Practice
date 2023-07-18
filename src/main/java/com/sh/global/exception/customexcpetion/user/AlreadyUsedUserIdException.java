package com.sh.global.exception.customexcpetion.user;

public class AlreadyUsedUserIdException extends RuntimeException {

    public AlreadyUsedUserIdException(String message) {
        super(message);
    }
}
