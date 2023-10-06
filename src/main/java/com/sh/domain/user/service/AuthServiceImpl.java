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
    public void logout(TokenDto token) {
        // access token null check
        checkAccessToken(token.getAccessToken());

        // refresh token null check
        checkRefreshToken(token.getRefreshToken());

        // access token 검증
        if (jwtProvider.validateToken(token.getAccessToken())) {
            // access token 에서 회원 아이디 정보를 가져옴.
            String id = jwtProvider.parseClaims(token.getAccessToken()).getSubject();

            // 회원 아이디로 Refresh Token 정보 조회, 없다면 이미 만료되어 삭제된 상태
            RefreshToken refreshToken =
                    refreshTokenRedisRepository
                            .findById(id)
                            .orElse(null);

            if(refreshToken != null) {
                // 해당 Refresh Token 정보 삭제
                userRedisService.deleteRefreshToken(refreshToken);

                // access token 의 남은 만료시간 정보를 가져옴.
                Long expiredTime = jwtProvider.getRemainExpiredTime(token.getAccessToken());

                // 로그아웃 한 access token 을 blacklist token 으로 등록
                // blacklist token 으로 등록하지 않으면, 서버는 해당 access token 이 로그아웃 처리된 토큰인지 모르고
                // 아직 유효시간이 만료되지 않은 토큰으로 인식해 오류가 발생하지 않음.
                // blacklist token 으로 등록함으로써 서버가 로그아웃 된 token 임을 알 수 있도록 하고 잘못된 접근을 방지함.
                userRedisService.saveBlackListToken(token.getAccessToken(), expiredTime);
            }
        }
    }

    // Access Token 재발급
    /**
     * Refresh Token 이 탈취된 상황을 방지해 Access Token 을 함께 요청받아 Access Token 으로 해당 Refresh Token 정보를 조회 및
     * 비교 1. Access Token, Refresh Token null check 2. Access Token 검증, 만료되지 않은 Access Token 일 경우
     * Exception 3. Refresh Token 검증, 이미 만료된 Refresh Token 일 경우 Exception 4. Access Token 값으로 Redis
     * 에 저장된 Refresh Token 정보(userId, Refresh Token, Access Token) 조회, 없을 경우 Exception 5. Redis 에서
     * 조회한 Refresh Token 값과 요청으로 넘어온 Refresh Token 값 비교, 다를 경우 Exception 6. 새로운 Access Token 과
     * Refresh Token 을 재발급하여 Redis 갱신 및 토큰 정보 반환
     */
    @Override
    public TokenDto accessTokenReIssue(TokenDto token) {
        // Access Token null check
        checkAccessToken(token.getAccessToken());

        // Refresh Token null check
        checkRefreshToken(token.getRefreshToken());

        // Access Token 값으로 Refresh Token 조회
        // Redis 에 존재하면 아직 유효한 Refresh Token
        // 존재하지 않으면 Exception
        RefreshToken rt =
                refreshTokenRedisRepository
                        .findByAccessToken(token.getAccessToken())
                        .orElseThrow(() -> TokenCustomException.EXPIRED_REFRESH_TOKEN);

        // Access Token 만료 정보 확인
        // 만약 Access Token 이 만료되지 않은 상태라면, 악의적인 요청으로 간주
        if (jwtProvider.validateTokenAndIsExpired(token.getAccessToken())) {
            // 기존에 저장되어 있던 Refresh Token 정보 삭제
            userRedisService.deleteRefreshToken(rt);

            // Access Token 값을 BlackList Token 으로 등록
            Long expiredTime = jwtProvider.getRemainExpiredTime(token.getAccessToken());
            userRedisService.saveBlackListToken(token.getAccessToken(), expiredTime);

            // 재로그인 요청
            throw TokenCustomException.UNAVAILABLE_TOKENS;
        }

        // Refresh Token 값 비교
        // 요청으로 넘어온 Refresh Token 값과 Redis 에서 조회한 Refresh Token 값이 다르다면, Access Token 을 탈취당한 경우로 간주
        // 기존에 저장되어 있던 Refresh Token 정보를 삭제하고, 재로그인 요청
        if (!token.getRefreshToken().equals(rt.getRefreshToken())) {
            userRedisService.deleteRefreshToken(rt);

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
