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

    @GetMapping("/api/v1/oauth/kakao/callback")
    @ResponseStatus(HttpStatus.OK)
    public String callbackKakao(String code) {
        return code;
    }

    // 카카오 로그인
    @PostMapping("/api/v1/oauth/kakao/login")
    @ResponseStatus(HttpStatus.OK)
    public OAuthLoginResponseDto kakaoLogin(@RequestBody KakaoLoginParams params) {
        return oAuthService.oauthLogin(params);
    }

    // 카카오 회원가입
    @PostMapping("/api/v1/oauth/kakao/join")
    @ResponseStatus(HttpStatus.CREATED)
    public Long kakaoJoin(@RequestBody OAuthSignupRequestDto signupRequest) {
        return oAuthService.oauthJoin(signupRequest);
    }

    // OAuth 로그아웃
    @TokenValueRequired
    @GetMapping("/api/v1/oauth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void oAuthLogout(TokenDto token) {
        oAuthService.oAuthLogout(token);
    }
}
