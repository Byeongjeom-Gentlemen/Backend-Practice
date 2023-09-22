package com.sh.global.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sh.global.oauth.OAuthInfoResponse;
import com.sh.global.oauth.OAuthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {

    @JsonProperty("id")
    private Long kakaoId;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        private KakaoProfile profile;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoProfile {
        private String nickname;

        @JsonProperty("profile_image_url")
        private String image;
    }

    @Override
    public String getOAuthProviderId() { return String.valueOf(kakaoId); }

    @Override
    public String getProfileNickname() {
        return kakaoAccount.profile.nickname;
    }

    @Override
    public String getProfileImage() {
        return kakaoAccount.profile.image;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
