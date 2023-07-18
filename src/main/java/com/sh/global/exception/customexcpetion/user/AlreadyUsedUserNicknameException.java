package com.sh.global.exception.customexcpetion.user;

public class AlreadyUsedUserNicknameException extends RuntimeException {

    public AlreadyUsedUserNicknameException(String message) {
        super(message);
    }
}
