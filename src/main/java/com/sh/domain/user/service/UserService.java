package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.LoginDto;
import com.sh.domain.user.dto.UserRequestDto;
import com.sh.domain.user.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    // 아이디 중복 확인
    boolean checkById(String id);

    // 닉네임 중복 확인
    boolean checkByNickname(String nickname);

    // 회원 생성
    @Transactional
    Long join(UserRequestDto user);
    
    // 로그인
    @Transactional
    UserResponseDto login(LoginDto loginDto);
    
    // 내 정보 조회
    UserResponseDto selectMe();

    // 회원 삭제
    void deleteUser(LoginDto user);
}
