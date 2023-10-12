package com.sh.domain.user.controller;

import com.sh.domain.user.dto.request.OAuthSignupRequestDto;
import com.sh.domain.user.dto.response.OAuthLoginResponseDto;
import com.sh.domain.user.service.OAuthService;
import com.sh.global.aop.DisableSwaggerSecurity;
import com.sh.global.oauth.kakao.KakaoLoginParams;
import com.sh.global.oauth.naver.NaverLoginParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "OAuth", description = "OAuth API")
public class OAuthController {

    private final OAuthService oAuthService;

    // 카카오 로그인 요청
    @Operation(
            summary = "카카오 로그인 API",
            description = "카카오 로그인 API 입니다. 카카오 서버에서 발급받은 인가코드 값을 필요로 합니다.")
    @DisableSwaggerSecurity
    @PostMapping("/api/v1/oauth/kakao")
    @ResponseStatus(HttpStatus.OK)
    public OAuthLoginResponseDto kakaoLogin(@RequestBody KakaoLoginParams params) {
        return oAuthService.oauthLogin(params);
    }

    // 네이버 로그인 요청
    @Operation(
            summary = "네이버 로그인 API",
            description = "네이버 로그인 API 입니다. 네이버 서버에서 발급받은 인가코드 값과 상태 값을 필요로 합니다.")
    @DisableSwaggerSecurity
    @PostMapping("/api/v1/oauth/naver")
    @ResponseStatus(HttpStatus.OK)
    public OAuthLoginResponseDto naverLogin(@RequestBody NaverLoginParams params) {
        return oAuthService.oauthLogin(params);
    }

    // OAuth 회원가입
    @Operation(
            summary = "OAuth 회원가입 API",
            description = "OAuth 회원의 가입 API 입니다. 각 OAuth 서비스의 회원 고유번호와 닉네임을 파라미터 값으로 받습니다.")
    @DisableSwaggerSecurity
    @PostMapping("/api/v1/oauth/{oauthProvider}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public Long oAuthJoin(
            @PathVariable String oauthProvider, @RequestBody OAuthSignupRequestDto signupRequest) {
        return oAuthService.oauthJoin(oauthProvider, signupRequest);
    }
}
