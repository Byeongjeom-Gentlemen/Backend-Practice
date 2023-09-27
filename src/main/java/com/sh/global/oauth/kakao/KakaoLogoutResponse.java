package com.sh.global.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sh.global.oauth.OAuthLogoutResponse;
import com.sh.global.oauth.OAuthProvider;

public class KakaoLogoutResponse implements OAuthLogoutResponse {

    @JsonProperty("id")
    Long kakaoId;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public Long getOAuthProviderId() {
        return this.kakaoId;
    }
}
