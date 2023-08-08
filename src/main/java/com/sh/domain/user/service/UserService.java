package com.sh.domain.user.service;

import com.sh.domain.user.dto.*;
import com.sh.global.util.jwt.TokenDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    // 회원 생성
    @Transactional
    Long join(SignupRequestDto signupRequest);

    // 로그인
    @Transactional
    UserLoginResponseDto login(LoginRequestDto loginRequest);

    // 내 정보 조회
    @Transactional(readOnly = true)
    UserBasicResponseDto selectMe();

    // 회원 삭제
    @Transactional
    void deleteUser();

    // 회원 수정(PATCH)
    @Transactional
    void modifyMe(UpdateUserRequestDto updateRequest);

    // 다른 회원 조회
    @Transactional(readOnly = true)
    UserBasicResponseDto selectOtherUser(Long userId);

    // 로그아웃
    void logout(String accessToken);
}
