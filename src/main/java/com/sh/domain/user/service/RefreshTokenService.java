package com.sh.domain.user.service;

import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenService {

    // Refresh Token 생성
    @Transactional
    void saveRefreshToken(String userId, String refreshToken);

    // Refresh Token 삭제
    @Transactional
    void deleteRefreshToken(String userId);

    @Transactional
    void checkExpiredRefreshToken(String userId);
}
