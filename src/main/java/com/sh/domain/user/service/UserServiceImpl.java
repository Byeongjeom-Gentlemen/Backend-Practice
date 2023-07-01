package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.UserDto;
import com.sh.domain.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public Long join(UserDto userDto) {
        // 아이디, 닉네임 중복확인 실패 시
        if(!checkById(userDto.getId()) || !checkByNickname(userDto.getNickname())) {
            return Long.MIN_VALUE;
        }
        // 비밀번호 암호화
        userDto.encryptPassword(passwordEncoder.encode(userDto.getPw()));
        // 회원 생성
        User user = userRepository.save(userDto.toEntity());
        // 성공 시 pk값 반환
        return user.getUserId();
    }
}
