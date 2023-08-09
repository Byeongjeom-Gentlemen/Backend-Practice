package com.sh.domain.user.service;

import com.sh.domain.user.dto.LoginRequestDto;
import com.sh.domain.user.dto.UserLoginResponseDto;
import com.sh.global.util.jwt.TokenDto;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    // 로그인
    @Transactional
    UserLoginResponseDto login(LoginRequestDto loginRequest);

    // Access Token 재발급
    @Transactional
    TokenDto accessTokenReIssue(TokenDto tokens);
}
