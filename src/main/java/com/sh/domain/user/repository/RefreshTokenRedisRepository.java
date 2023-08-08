package com.sh.domain.user.repository;

import com.sh.domain.user.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    // Access Token 재발급, Access Token 값으로 Refresh Token 확인
    Optional<RefreshToken> findByAccessToken(String accessToken);
}
