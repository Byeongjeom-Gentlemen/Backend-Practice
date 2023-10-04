package com.sh.global.oauth.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sh.global.oauth.OAuthInfoResponse;
import com.sh.global.oauth.OAuthProvider;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {

    @JsonProperty("resultcode")
    private String resultCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("response")
    private Response response;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {

        @JsonProperty("id")
        private String naverId;

        @JsonProperty("nickname")
        private String nickname;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public String getOAuthProviderId() {
        return response.naverId;
    }

    @Override
    public String getProfileNickname() {
        return response.nickname;
    }
}
