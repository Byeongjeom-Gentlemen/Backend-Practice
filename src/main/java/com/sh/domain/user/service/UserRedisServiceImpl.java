package com.sh.domain.user.service;

import com.sh.domain.user.domain.BlackListToken;
import com.sh.domain.user.domain.RefreshToken;
import com.sh.domain.user.repository.BlackListTokenRedisRepository;
import com.sh.domain.user.repository.RefreshTokenRedisRepository;
import com.sh.global.exception.customexcpetion.UserCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRedisServiceImpl implements UserRedisService {

    // Refresh Token 만료시간(초 단위)
    private static final Long REFRESH_TOKEN_EXPIRED_TIME = 120000L; // 7일 1000L * 60L * 60L * 24L * 7L

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final BlackListTokenRedisRepository blackListTokenRedisRepository;

    // Refresh Token 저장
    @Override
    public void saveRefreshToken(String id, String refreshToken, String accessToken) {
        // 잘못된 접근인지 아닌지 검증(이미 Refresh Token이 존재한다면 이미 로그인되어 있는 상태, 로그아웃 하지 않고 로그인을 재 요청한 경우)
        existsByRefreshToken(id);

        RefreshToken rt =
                RefreshToken.builder()
                        .id(id)
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .expiredTime(REFRESH_TOKEN_EXPIRED_TIME)
                        .build();

        refreshTokenRedisRepository.save(rt);
    }

    private void existsByRefreshToken(String id) {
        // 이미 사용자의 Refresh Token이 존재한다면 로그아웃을 하지 않고 로그인을 재 요청한 경우
        if (refreshTokenRedisRepository.existsById(id)) {
            throw UserCustomException.ALREADY_LOGIN;
        }
    }

    // Refresh Token 삭제
    @Override
    public void deleteRefreshToken(RefreshToken refreshToken) {
        // Refresh Token 삭제
        refreshTokenRedisRepository.delete(refreshToken);
    }

    // BlackList Token 등록
    @Override
    public void saveBlackListToken(String accessToken, Long expiredTime) {
        BlackListToken blackListToken =
                BlackListToken.builder().accessToken(accessToken).expiredTime(expiredTime).build();

        blackListTokenRedisRepository.save(blackListToken);
    }

    // BlackList Token 조회 및 확인
    @Override
    public boolean checkBlackListToken(String accessToken) {
        return blackListTokenRedisRepository.existsById(accessToken);
    }
}
