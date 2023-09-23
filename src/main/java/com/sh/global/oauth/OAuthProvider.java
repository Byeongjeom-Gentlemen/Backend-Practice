package com.sh.global.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sh.global.exception.customexcpetion.OAuthCustomException;

import java.util.stream.Stream;

public enum OAuthProvider {
    KAKAO;

    @JsonCreator
    public static OAuthProvider parsing(String requestValue) {
        return Stream.of(OAuthProvider.values())
                .filter(oAuthProvider -> oAuthProvider.toString().equals(requestValue.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> OAuthCustomException.UNSUPPORTED_OAUTH_PROVIDER);
    }
}
