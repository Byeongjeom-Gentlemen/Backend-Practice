package com.sh.domain.user.service;

import com.sh.domain.user.domain.RefreshToken;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.request.LoginRequestDto;
import com.sh.domain.user.dto.response.UserLoginResponseDto;
import com.sh.domain.user.repository.RefreshTokenRedisRepository;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.customexcpetion.TokenCustomException;
import com.sh.global.exception.customexcpetion.UserCustomException;
import com.sh.global.util.CustomUserDetails;
import com.sh.global.util.jwt.JwtProvider;
import com.sh.global.util.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRedisService userRedisService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    // 로그인
    @Override
    public UserLoginResponseDto login(LoginRequestDto loginRequest) {
        try {
            // id / pw를 기반으로 Authentication 객체 생성 및 실제 검증 (아이디 존재 여부, 사용자 비밀번호 체크)
            // 아이디가 존재하지 않거나 id와 비밀번호가 맞지 않으면 예외처리
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getId(), loginRequest.getPw()));

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 인증정보를 기반으로 토큰 생성
            String accessToken = jwtProvider.generateAccessToken(customUserDetails);
            String refreshToken = jwtProvider.generateRefreshToken();

            // Redis에 저장 (access token과 refresh token 값을 유저와 1대1로 저장)
            userRedisService.saveRefreshToken(
                    customUserDetails.getUsername(), refreshToken, accessToken);

            // 토큰정보
            TokenDto token = TokenDto.of(accessToken, refreshToken);

            return UserLoginResponseDto.of(customUserDetails, token);

        } catch (BadCredentialsException e) {
            throw UserCustomException.NOT_MATCHED_USER;
        }
    }

    // 로그아웃
    @Override
    public void logout() {
        User user = userService.getLoginUser();
        RefreshToken token = queryToken(user.getId());

        if (token.getAccessToken() != null && token.getRefreshToken() != null) {
            // Redis Token 값 삭제
            userRedisService.deleteRefreshToken(token);

            // BlackList Token 저장
            userRedisService.saveBlackListToken(token.getAccessToken());
        }
    }

    // 회원의 토큰 정보 가져오기
    private RefreshToken queryToken(String id) {
        return refreshTokenRedisRepository
                .findById(id)
                .orElseThrow(() -> TokenCustomException.NON_TOKEN);
    }

    // Access Token 재발급
    // Refresh Token 이 탈취된 상황을 방지해 Access Token 을 함께 요청받아 Access Token 으로 해당 Refresh Token 정보를 조회 및
    // 비교
    // 1. Access Token, Refresh Token null check
    // 2. Access Token 검증, 만료되지 않은 Access Token 일 경우 Exception
    // 3. Refresh Token 검증, 이미 만료된 Refresh Token 일 경우 Exception
    // 4. Access Token 값으로 Redis 에 저장된 Refresh Token 정보(userId, Refresh Token, Access Token) 조회, 없을
    // 경우 Exception
    // 5. Redis 에서 조회한 Refresh Token 값과 요청으로 넘어온 Refresh Token 값 비교, 다를 경우 Exception
    // 6. 새로운 Access Token 과 Refresh Token 을 재발급하여 Redis 갱신 및 토큰 정보 반환
    @Override
    public TokenDto accessTokenReIssue(String accessToken, String refreshToken) {
        // Access Token 값 check
        checkAccessToken(accessToken);

        // Refresh Token 값 check
        checkRefreshToken(refreshToken);

        // Access Token 값으로 Refresh Token 조회
        // Redis 에 존재하면 아직 유효한 Refresh Token
        // 존재하지 않으면 Exception (Refresh Token 만료 or Access Token 값 오류)
        RefreshToken rt = userRedisService.selectRefreshToken(accessToken);

        // Access Token 만료 정보 확인
        // 만약 Access Token 이 만료되지 않은 상태라면, 악의적인 요청으로 간주
        // 기존에 저장되어 있던 Refresh Token 정보를 삭제하고, Access Token 값을 BlackList Token 으로 등록, 재로그인 요청
        if (jwtProvider.validateTokenAndIsExpired(accessToken)) {
            userRedisService.deleteRefreshToken(rt);
            userRedisService.saveBlackListToken(accessToken);
            throw TokenCustomException.UNAVAILABLE_TOKENS;
        }

        // Refresh Token 값 비교
        // 요청으로 넘어온 Refresh Token 값과 Redis 에서 조회한 Refresh Token 값이 다르다면, Access Token 을 탈취당한 경우로 간주
        // 기존에 저장되어 있던 Refresh Token 정보를 삭제하고, 재로그인 요청
        if (!refreshToken.equals(rt.getRefreshToken())) {
            userRedisService.saveBlackListToken(accessToken);
            throw TokenCustomException.UNAVAILABLE_TOKENS;
        }

        User user =
                userRepository
                        .findById(rt.getId())
                        .orElseThrow(() -> UserCustomException.USER_NOT_FOUND);
        CustomUserDetails customUserDetails = CustomUserDetails.from(user);

        // Refresh Token Rotation (Access Token 재발급 시 Refresh Token 도 재발급)
        String newAccessToken = jwtProvider.generateAccessToken(customUserDetails);
        String newRefreshToken = jwtProvider.generateRefreshToken();

        // Redis 갱신
        RefreshToken newRt =
                RefreshToken.builder()
                        .id(user.getId())
                        .refreshToken(newRefreshToken)
                        .accessToken(newAccessToken)
                        .build();

        refreshTokenRedisRepository.save(newRt);

        return TokenDto.of(newAccessToken, newRefreshToken);
    }

    // Access Token 값 확인
    private void checkAccessToken(String accessToken) {
        if (accessToken == null) {
            throw TokenCustomException.NON_ACCESS_TOKEN_REQUEST_HEADER;
        }
    }

    // Refresh Token 값 확인
    private void checkRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw TokenCustomException.NON_REFRESH_TOKEN_REQUEST_HEADER;
        }
    }
}
