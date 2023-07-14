package com.sh.global.common;

import lombok.Getter;

@Getter
public enum UserStatus {

    // 활동 유저
    ALIVE_USER("alive"),
    // 휴먼 유저
    DORMANT_USER("dormant"),
    // 탈퇴한 유저
    WITHDRAWAL_USER("withdrawn");


    private final String status;

    UserStatus(final String status) {
        this.status = status;
    }
}
