package com.sh.domain.user.service;

import com.sh.domain.user.dto.*;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    // 아이디 중복 확인
    boolean checkById(String id);

    // 닉네임 중복 확인
    boolean checkByNickname(String nickname);

    // 회원 생성
    @Transactional
    Long join(SignupRequestDto user);
    
    // 로그인
    @Transactional
    UserLoginResponseDto login(UserBasicRequestDto userBasicRequestDto);
    
    // 내 정보 조회
    UserBasicResponseDto selectMe();

    @Transactional
    // 회원 삭제
    void deleteUser(UserBasicRequestDto user);

    // 회원 수정(PATCH)
    @Transactional
    void modifyMe(UpdateUserRequestDto user);

    // 다른 회원 조회
    UserBasicResponseDto selectOtherUser(Long id);
}
