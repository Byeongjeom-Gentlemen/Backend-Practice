package com.sh.domain.user.controller;

import antlr.Token;
import com.sh.domain.user.dto.request.OAuthSignupRequestDto;
import com.sh.domain.user.dto.response.OAuthLoginResponseDto;
import com.sh.domain.user.dto.response.UserLoginResponseDto;
import com.sh.domain.user.service.AuthService;
import com.sh.domain.user.service.OAuthService;
import com.sh.global.aop.TokenValueRequired;
import com.sh.global.oauth.kakao.KakaoLoginParams;
import com.sh.global.util.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    // OAuth 로그인
    @PostMapping("/api/v1/oauth/login/{oauthProvider}")
    @ResponseStatus(HttpStatus.OK)
    public OAuthLoginResponseDto kakaoLogin(@PathVariable String oauthProvider, @RequestBody KakaoLoginParams params) {
        return oAuthService.oauthLogin(oauthProvider, params);
    }

    // OAuth 회원가입
    @PostMapping("/api/v1/oauth/join/{oauthProvider}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long kakaoJoin(@PathVariable String oauthProvider, @RequestBody OAuthSignupRequestDto signupRequest) {
        return oAuthService.oauthJoin(oauthProvider, signupRequest);
    }

    // OAuth 로그아웃
    @TokenValueRequired
    @GetMapping("/api/v1/oauth/logout/{oauthProvider}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void oAuthLogout(@PathVariable String oauthProvider, TokenDto token) {
        oAuthService.oAuthLogout(oauthProvider, token);
    }
}
