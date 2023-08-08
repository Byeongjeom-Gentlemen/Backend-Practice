package com.sh.domain.user.service;

import com.sh.domain.user.domain.RefreshToken;

public interface UserRedisService {

    // Refresh Token 저장
    void saveRefreshToken(String id, String accessToken, String refreshToken);

    // Refresh Token 조회
    RefreshToken selectRefreshToken(String accessToken);

    // Refresh Token 삭제
    void deleteRefreshToken(RefreshToken refreshToken);

    // BlackList Token 저장
    void saveBlackListToken(String accessToken);

    // BlackList Token 조회 및 확인
    boolean checkBlackListToken(String accessToken);
}
