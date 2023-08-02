package com.sh.domain.user.service;

import com.sh.domain.user.domain.RefreshToken;
import com.sh.domain.user.repository.RefreshTokenRepository;
import com.sh.global.exception.customexcpetion.user.AlreadyLoginException;
import com.sh.global.exception.customexcpetion.token.NonTokenException;
import com.sh.global.exception.errorcode.TokenErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

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
    public void deleteRefreshToken(String refreshToken) {
        RefreshToken rt = refreshTokenRepository.findByRefreshTokenName(refreshToken)
                .orElseThrow(() -> new NonTokenException(TokenErrorCode.NOT_FOUND_TOKEN));

        refreshTokenRepository.delete(rt);
    }
}
