package com.sh.domain.user.domain;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "blacklistToken")
public class BlackListToken {

    @Id private String accessToken;

    private String value;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiredTime;

    @Builder
    private BlackListToken(String accessToken, long expiredTime) {
        this.accessToken = accessToken;
        this.value = "logout";
        this.expiredTime = expiredTime;
    }
}
