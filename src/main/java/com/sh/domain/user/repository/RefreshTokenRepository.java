package com.sh.domain.user.repository;

import com.sh.domain.user.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // refresh token 존재여부 확인
    boolean existsByUserId(String userId);

    // refresh token 저장
    RefreshToken save(RefreshToken refreshToken);

    // 회원 아이디로 refresh token 가져오기
    Optional<RefreshToken> findByUserId(String userId);

    // Refresh Token 이름으로 token 가져오기
    Optional<RefreshToken> findByRefreshTokenName(String refreshTokenName);

    // refresh token 삭제
    void delete(RefreshToken refreshToken);
}
