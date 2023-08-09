package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.request.SignupRequestDto;
import com.sh.domain.user.dto.request.UpdateUserRequestDto;
import com.sh.domain.user.dto.response.UserBasicResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    // 회원 생성
    @Transactional
    Long join(SignupRequestDto signupRequest);

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

    // 로그인된 회원 정보 가져오기
    @Transactional(readOnly = true)
    User getLoginUser();
}
