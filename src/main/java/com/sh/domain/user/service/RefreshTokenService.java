package com.sh.domain.user.service;

import com.sh.domain.user.domain.RefreshToken;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Ref;

public interface RefreshTokenService {

    // Refresh Token 생성
    void saveRefreshToken(String userId, String refreshToken);

    // Refresh Token 삭제
    @Transactional
    void deleteRefreshToken(String refreshToken);
}
