package com.sh.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 토큰을 저장하기 위한 객체
public class TokenDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireIn;
    private Long refreshTokenExpireIn;
    private String authority;
    private String info;

    public void setInfo(String info) {
        this.info = info;
    }

}
