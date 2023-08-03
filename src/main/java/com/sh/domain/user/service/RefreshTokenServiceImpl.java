package com.sh.domain.user.service;

import com.sh.domain.user.domain.RefreshToken;
import com.sh.domain.user.repository.RefreshTokenRepository;
import com.sh.global.exception.customexcpetion.token.ExpiredTokenException;
import com.sh.global.exception.customexcpetion.user.AlreadyLoginException;
import com.sh.global.exception.customexcpetion.token.NonTokenException;
import com.sh.global.exception.errorcode.TokenErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final long refreshTokenExpirationMillis = 604800000;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    // Refresh Token 생성 및 저장
    @Override
    public void saveRefreshToken(String userId, String token) {
        existByRefreshToken(userId);

        // Refresh Token 생성 및 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .refreshTokenName(token)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    // Refresh Token 확인
    private void existByRefreshToken(String userId) {
        // 이미 사용자의 Refresh Token이 존재한다면 로그아웃을 하지 않고 재로그인을 요청한 경우
        if(refreshTokenRepository.existsByUserId(userId)) {
            throw new AlreadyLoginException(UserErrorCode.ALREADY_LOGIN);
        }
    }

    // Refresh Token 삭제
    @Override
    public void deleteRefreshToken(String userId) {
        RefreshToken rt = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new NonTokenException(TokenErrorCode.NOT_FOUND_TOKEN));

        refreshTokenRepository.delete(rt);
    }


    // Access Token 재발급 요청 시 Refresh Token이 만료되었는지 확인
    @Override
    public void checkExpiredRefreshToken(String userId) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new NonTokenException(TokenErrorCode.NOT_FOUND_TOKEN));

        // Refresh Token이 생성된 시간
        LocalDateTime startTime = refreshToken.getCreatedDate();
        // 현재 시간
        LocalDateTime curTime = LocalDateTime.now();

        // Refresh Token이 생성된 시간과 현재 시간 간 차이 구하기
        Duration diff = Duration.between(startTime.toLocalTime(), curTime.toLocalTime());

        System.out.println(diff.toMillis());
        
        // 두 시간의 차이가 Refresh Token Expiration Millis보다 같거나 크다면 만료날짜가 지난 것
        // Refresh Token을 삭제하고 예외처리
        if(diff.toMillis() >= refreshTokenExpirationMillis) {
            deleteRefreshToken(userId);
            throw new ExpiredTokenException(TokenErrorCode.EXPIRED_REFRESH_TOKEN);
        }
    }
}
