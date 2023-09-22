package com.sh.global.oauth.kakao;

import com.sh.global.oauth.OAuthLoginParams;
import com.sh.global.oauth.OAuthProvider;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KakaoLoginParams implements OAuthLoginParams {

    @Getter
    private String authorizationCode;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);

        return body;
    }
}
