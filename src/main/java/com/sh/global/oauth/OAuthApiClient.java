package com.sh.global.oauth;

public interface OAuthApiClient {
    // Client 타입 반환
    OAuthProvider oAuthProvider();

    // Authorization Code 를 기반으로 인증 API 요청 후 Access Token 값 획득
    String requestAccessToken(OAuthLoginParams params);

    // Access Token 을 기반으로 해당 사용자의 정보 획득
    OAuthInfoResponse requestOauthInfo(String accessToken);

    // 로그아웃
    OAuthLogoutResponse requestOauthLogout(Long oauthId);
}
