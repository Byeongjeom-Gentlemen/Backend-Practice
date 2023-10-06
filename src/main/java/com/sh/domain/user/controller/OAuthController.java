package com.sh.domain.user.controller;

import com.sh.domain.user.dto.request.OAuthSignupRequestDto;
import com.sh.domain.user.dto.response.OAuthLoginResponseDto;
import com.sh.domain.user.service.OAuthService;
import com.sh.global.aop.TokenValueRequired;
import com.sh.global.oauth.kakao.KakaoLoginParams;
import com.sh.global.oauth.naver.NaverLoginParams;
import com.sh.global.util.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    // 카카오 로그인 요청
    @PostMapping("/api/v1/oauth/kakao/login")
    @ResponseStatus(HttpStatus.OK)
    public OAuthLoginResponseDto kakaoLogin(@RequestBody KakaoLoginParams params) {
        return oAuthService.oauthLogin(params);
    }

    // 네이버 로그인 요청
    @PostMapping("/api/v1/oauth/naver/login")
    @ResponseStatus(HttpStatus.OK)
    public OAuthLoginResponseDto naverLogin(@RequestBody NaverLoginParams params) {
        return oAuthService.oauthLogin(params);
    }

    // OAuth 회원가입
    @PostMapping("/api/v1/oauth/join/{oauthProvider}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long oAuthJoin(
            @PathVariable String oauthProvider, @RequestBody OAuthSignupRequestDto signupRequest) {
        return oAuthService.oauthJoin(oauthProvider, signupRequest);
    }
}
