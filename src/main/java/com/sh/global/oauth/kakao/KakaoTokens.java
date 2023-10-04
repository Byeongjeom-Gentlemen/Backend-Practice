package com.sh.global.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

// 카카오 로그인 토큰 요청 결과 값
@Getter
public class KakaoTokens {

    // access token
    @JsonProperty("access_token")
    private String accessToken;

    // 토큰 종류
    @JsonProperty("token_type")
    private String tokenType;

    // refresh token
    @JsonProperty("refresh_token")
    private String refreshToken;

    // 토큰 만료 시간
    @JsonProperty("expires_in")
    private Integer expiresIn;

    // refresh token 만료 시간
    @JsonProperty("refresh_token_expires_in")
    private Integer refreshTokenExpiresIn;

    // 제공 데이터 종류
    @JsonProperty("scope")
    private String scope;
}
