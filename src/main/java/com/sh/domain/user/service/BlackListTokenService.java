package com.sh.domain.user.service;

import org.springframework.transaction.annotation.Transactional;

public interface BlackListTokenService {

    @Transactional
    void createBlackListToken(String accessToken);

    @Transactional(readOnly = true)
    boolean checkBlackListToken(String accessToken);

}
