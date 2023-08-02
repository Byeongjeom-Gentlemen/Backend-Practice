package com.sh.domain.user.service;

import com.sh.domain.user.domain.BlackListToken;
import com.sh.domain.user.repository.BlackListTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListTokenServiceImpl implements BlackListTokenService{

    private final BlackListTokenRepository blackListTokenRepository;

    // BlackListToken 생성
    @Override
    public void createBlackListToken(String accessToken) {
        BlackListToken token = BlackListToken.builder().accessToken(accessToken).build();
        blackListTokenRepository.save(token);
    }

    // 해당 BlackListToken 확인
    @Override
    public boolean checkBlackListToken(String accessToken) {
        return blackListTokenRepository.existsByAccessToken(accessToken);
    }
}
