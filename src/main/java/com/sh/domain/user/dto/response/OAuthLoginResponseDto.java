package com.sh.domain.user.dto.response;

import com.sh.global.oauth.OAuthInfoResponse;
import com.sh.global.oauth.OAuthProvider;
import com.sh.global.util.jwt.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class OAuthLoginResponseDto {

    private boolean isLogin;
    private String oAuthProvider;
    private String oAuthProviderId;
    private String nickname;
    private TokenDto token;

    public static OAuthLoginResponseDto of (boolean isLogin, OAuthInfoResponse oAuthInfoResponse, TokenDto token) {
        return OAuthLoginResponseDto.builder()
                .isLogin(isLogin)
                .oAuthProvider(String.valueOf(oAuthInfoResponse.getOAuthProvider()))
                .oAuthProviderId(oAuthInfoResponse.getOAuthProviderId())
                .nickname(oAuthInfoResponse.getProfileNickname())
                .token(token)
                .build();
    }
}
