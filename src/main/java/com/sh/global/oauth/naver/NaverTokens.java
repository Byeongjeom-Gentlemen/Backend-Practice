package com.sh.global.oauth.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverTokens {

    // access token
    @JsonProperty("access_token")
    private String accessToken;

    // refresh token
    @JsonProperty("refresh_token")
    private String refreshToken;

    // 토큰 종류
    @JsonProperty("token_type")
    private String tokenType;

    // 토큰 만료시간
    @JsonProperty("expires_in")
    private String expiresIn;
}
