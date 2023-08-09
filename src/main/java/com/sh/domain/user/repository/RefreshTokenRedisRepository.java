package com.sh.domain.user.repository;

import com.sh.domain.user.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    // Access Token 재발급, Access Token 값으로 Refresh Token 확인
    Optional<RefreshToken> findByAccessToken(String accessToken);
}
