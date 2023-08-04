package com.sh.domain.user.controller;

import com.sh.domain.user.service.AuthService;
import com.sh.global.util.jwt.JwtProvider;
import com.sh.global.util.jwt.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    // Access Token 재발급
    @Operation(
            summary = "Access Token 재발급 API",
            description = "Request Header에 있는 Refresh Token 값으로 Access Token을 재발급 받는 API 입니다.")
    @GetMapping("/api/v1/auth/reissue")
    public ResponseEntity<TokenDto> accessTokenReIssue(HttpServletRequest request) {
        String refreshToken = jwtProvider.resolveRefreshToken(request);

        return ResponseEntity.ok().body(authService.accessTokenReIssue(refreshToken));
    }
}
