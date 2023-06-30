package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.UserDto;
import com.sh.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // 아이디 중복 확인
    @Override
    public boolean checkById(String id) {
        return userRepository.existsByUserId(id);
    }

    // 닉네임 중복 확인
    @Override
    public boolean checkByNickname(String nickname) {
        return userRepository.existByNickname(nickname);
    }

    // 회원 생성
    @Override
    public void join(UserDto users) {
        // 아이디, 닉네임 중복확인 실패 시
        if(!checkById(users.getId()) || !checkByNickname(users.getNickname())) {
            // 로직
        }

        // 성공 시
        // 비밀번호 암호화
        // user 객체를 리턴
    }
}
