package com.sh.global.oauth.naver;

import com.sh.global.oauth.OAuthLoginParams;
import com.sh.global.oauth.OAuthProvider;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class NaverLoginParams implements OAuthLoginParams {

    @Getter private String authorizationCode;

    @Getter private String state;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("state", state);

        return body;
    }
}
