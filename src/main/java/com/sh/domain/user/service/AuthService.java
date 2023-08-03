package com.sh.domain.user.service;

import com.sh.global.util.jwt.TokenDto;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {

    @Transactional
    TokenDto accessTokenReIssue(String refreshToken);
}
