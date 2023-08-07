package com.sh.domain.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken")
public class RefreshToken {

    @Id
    private String userId;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiredTime;

    @Builder
    private RefreshToken(String userId, String refreshToken, long expiredTime) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiredTime = expiredTime;
    }
}
