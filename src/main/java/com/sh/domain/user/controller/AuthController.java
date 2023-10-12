package com.sh.domain.user.controller;

import com.sh.domain.user.dto.request.LoginRequestDto;
import com.sh.domain.user.dto.response.UserLoginResponseDto;
import com.sh.domain.user.service.AuthService;
import com.sh.global.aop.DisableSwaggerSecurity;
import com.sh.global.aop.TokenValueRequired;
import com.sh.global.util.jwt.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

    private final AuthService authService;

    // 로그인
    @Operation(summary = "로그인 API", description = "로그인하는 API 입니다. 로그인시에는 Id, Password 값이 필요합니다.")
    @DisableSwaggerSecurity
    @PostMapping("/api/v1/auth/login")
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto login(
            @RequestBody @Valid LoginRequestDto loginRequest) {
        return authService.login(loginRequest);
    }

    // 로그아웃
    @Operation(summary = "로그아웃 API", description = "회원 로그아웃하는 API 입니다. 로그인이 되어 있는 상태여야 합니다.")
    @DisableSwaggerSecurity
    @TokenValueRequired
    @GetMapping("/api/v1/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(TokenDto token) {
        authService.logout(token);
    }

    // Access Token 재발급
    @Operation(
            summary = "Access Token 재발급 API",
            description = "Request Header에 있는 Refresh Token 값으로 Access Token을 재발급 받는 API 입니다.")
    @DisableSwaggerSecurity
    @TokenValueRequired
    @GetMapping("/api/v1/auth/reissue")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto accessTokenReIssue(TokenDto token) {
        return authService.accessTokenReIssue(token);
    }
}
