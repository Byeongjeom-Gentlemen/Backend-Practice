package com.sh.domain.user.service;

import com.sh.domain.user.domain.RefreshToken;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.LoginRequestDto;
import com.sh.domain.user.dto.UserLoginResponseDto;
import com.sh.domain.user.repository.RefreshTokenRedisRepository;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.customexcpetion.token.NonTokenException;
import com.sh.global.exception.customexcpetion.token.UnauthorizedTokenException;
import com.sh.global.exception.customexcpetion.user.NotMatchesUserException;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.exception.errorcode.TokenErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
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
            userRedisService.saveRefreshToken(customUserDetails.getUsername(), refreshToken, accessToken);

            // 토큰정보
            TokenDto token = TokenDto.of(accessToken, refreshToken);

            return UserLoginResponseDto.from(customUserDetails, token);

        } catch (BadCredentialsException e) {
            throw new NotMatchesUserException(UserErrorCode.INVALID_AUTHENTICATION);
        }
    }

    // Access Token 재발급
    @Override
    public TokenDto accessTokenReIssue(TokenDto token) {
        String accessToken = token.getAccessToken();
        String refreshToken = token.getRefreshToken();

        // access token 값이 null 인 경우
        verifiedAccessToken(accessToken);
        
        // refresh token 값이 null 인 경우
        verifiedRefreshToken(refreshToken);

        // Refresh Token 검증
        jwtProvider.validateToken(refreshToken);

        // 만료된 Access Token 값으로 Refresh Token 조회
        // Refresh Token 이 존재하지 않는다면 Refresh Token 값도 만료된 상황으로 재 로그인 필요 예외처리
        RefreshToken rt = userRedisService.selectRefreshToken(accessToken);

        // Refresh Token 이 존재한다면, 요청으로 넘어온 Refresh Token과 Redis에 저장되어 있는 Refresh Token을 비교
        // 두 Refresh Token 값이 일치하지 않는다면, 비정상적인 접근으로 처리
        if(!refreshToken.equals(rt.getRefreshToken())) {
            userRedisService.deleteRefreshToken(rt);
            userRedisService.saveBlackListToken(accessToken);
            throw new UnauthorizedTokenException(TokenErrorCode.UNAVAILABLE_TOKENS);
        }

        // CustomUserDetails 형으로 변환
        User user = findUserById(rt.getId());
        CustomUserDetails customUserDetails = CustomUserDetails.from(user);

        // Refresh Token Rotation (Access Token 재발급 시 Refresh Token도 재발급)
        String newAccessToken = jwtProvider.generateAccessToken(customUserDetails);
        String newRefreshToken = jwtProvider.generateRefreshToken();

        // Redis 갱신
        RefreshToken newRt = RefreshToken.builder().id(user.getId()).refreshToken(newRefreshToken).accessToken(newAccessToken).build();
        refreshTokenRedisRepository.save(newRt);

        return TokenDto.of(newAccessToken, newRefreshToken);
    }

    // Access Token 값 확인
    private void verifiedAccessToken(String accessToken) {
        if(accessToken == null) {
            throw new NonTokenException(TokenErrorCode.NON_ACCESS_TOKEN_REQUEST_HEADER);
        }
    }

    // Refresh Token 값 확인
    private void verifiedRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new NonTokenException(TokenErrorCode.NON_REFRESH_TOKEN_REQUEST_HEADER);
        }
    }

    // 회원정보 반환
    private User findUserById(String id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        return user;
    }
}
