package com.sh.domain.user.util;

import lombok.Getter;

@Getter
public enum UserStatus {

    // 활동 유저
    ALIVE,
    // 휴먼 유저
    DORMANT,
    // 탈퇴한 유저
    WITHDRAWN;
}
