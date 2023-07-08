package com.sh.global.exception.customexcpetion;

public class AlreadyUsedUserNicknameException extends RuntimeException {

    public AlreadyUsedUserNicknameException(String message) {
        super(message);
    }
}
