package com.sh.domain.user.service;

import com.sh.domain.user.dto.LoginDto;
import com.sh.domain.user.dto.UserRequestDto;
import com.sh.domain.user.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    // 아이디 중복 확인
    boolean checkById(String id);

    // 닉네임 중복 확인
    boolean checkByNickname(String nickname);

    // 회원 생성
    @Transactional
    Long join(UserRequestDto user) throws Exception;

    @Transactional
    UserResponseDto login(LoginDto loginDto) throws Exception;
}
