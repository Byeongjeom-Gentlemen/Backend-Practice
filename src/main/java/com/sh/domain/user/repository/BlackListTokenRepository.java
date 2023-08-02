package com.sh.domain.user.repository;

import com.sh.domain.user.domain.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Long> {

    // 블랙리스트 토큰 저장
    BlackListToken save(BlackListToken blackListToken);

    // 블랙리스트 토큰에 값이 있는지 확인(검증 요청할 때 마다) -> Spring Security에서 처리
    boolean existsByAccessToken(String accessToken);
}
