package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.customexcpetion.token.NonTokenException;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.exception.errorcode.TokenErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.util.CustomUserDetails;
import com.sh.global.util.jwt.JwtProvider;
import com.sh.global.util.jwt.TokenDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    // Access Token 재발급
    @Override
    public TokenDto accessTokenReIssue(String refreshToken) {
        // refresh token 값이 없는 경우
        verifiedRefreshToken(refreshToken);

        // Refresh Token 복호화 && 복호화된 정보에서 회원 아이디 가져오기
        Claims claims = jwtProvider.parseClaims(refreshToken);
        String userId = claims.getSubject();

        // 회원 아이디로 Refresh Token이 만료되었는지 확인
        // 1. Refresh Token 존재유무 확인, 존재하지 않는다면 Exception
        // 2. Refresh Token이 존재한다면 만료되었는지 확인, 만료되었다면 Exception(재로그인 필요)
        // 3. 만료되지 않았다면 Access Token 재발급 로직 수행
        refreshTokenService.checkExpiredRefreshToken(userId);

        // 새로운 Access Token 발급 (Refresh Token은 재발급 받지 않는다고 가정)
        User user = findUserById(userId);
        CustomUserDetails customUserDetails = CustomUserDetails.from(user);
        String newAccessToken = jwtProvider.generateAccessToken(customUserDetails);

        return TokenDto.of(newAccessToken, refreshToken);
    }

    // Refresh Token 값 확인
    private void verifiedRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new NonTokenException(TokenErrorCode.NON_REFRESH_TOKEN_REQUEST_HEADER);
        }
    }

    // Refresh Token을 복호화하여 subject로 회원 정보를 반환
    private User findUserById(String id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        return user;
    }
}
