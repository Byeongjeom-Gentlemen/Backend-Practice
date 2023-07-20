package com.sh.domain.user.service;

import com.sh.domain.user.dto.*;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    // 회원 생성
    @Transactional
    Long join(SignupRequestDto signupRequest);

    // 로그인
    @Transactional
    UserLoginResponseDto login(LoginRequestDto loginRequest);

    @Transactional(readOnly = true)
    // 내 정보 조회
    UserBasicResponseDto selectMe();

    @Transactional
    // 회원 삭제
    void deleteUser();

    // 회원 수정(PATCH)
    @Transactional
    void modifyMe(UpdateUserRequestDto updateRequest);

    @Transactional(readOnly = true)
    // 다른 회원 조회
    UserBasicResponseDto selectOtherUser(Long id);
}
