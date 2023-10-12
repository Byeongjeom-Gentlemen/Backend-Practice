package com.sh.domain.user.domain;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken")
public class RefreshToken {

    @Id private String id;

    private String refreshToken;

    // @Indexed 어노테이션이 있으면 해당 필드 값으로 데이터를 찾아올 수 있음.
    @Indexed private String accessToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiredTime;

    @Builder
    private RefreshToken(String id, String refreshToken, String accessToken, Long expiredTime) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.expiredTime = expiredTime;
    }
}
