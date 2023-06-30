package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService {

    // 아이디 중복 확인
    boolean checkById(String id);

    // 닉네임 중복 확인
    boolean checkByNickname(String nickname);

    // 회원 생성
    @Transactional
    void join(UserDto user);
}
