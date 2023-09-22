package com.sh.global.oauth;

public interface OAuthInfoResponse {
    String getOAuthProviderId();
    String getProfileNickname();
    String getProfileImage();
    OAuthProvider getOAuthProvider();
}
