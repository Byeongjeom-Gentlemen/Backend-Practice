package com.sh.domain.user.service;

import com.sh.domain.user.dto.request.LoginRequestDto;
import com.sh.domain.user.dto.response.UserLoginResponseDto;
import com.sh.global.util.jwt.TokenDto;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    // 로그인
    @Transactional
    UserLoginResponseDto login(LoginRequestDto loginRequest);

    // 로그아웃
    void logout(String accessToken);

    // Access Token 재발급
    @Transactional
    TokenDto accessTokenReIssue(TokenDto tokens);
}
