package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.UserDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*@Service */
public interface UserService {

    // 아이디 중복 확인
    boolean checkById(String id);

    // 닉네임 중복 확인
    boolean checkByNickname(String nickname);

    // 회원 생성
    /* @Transactional */
    Long join(UserDto user);
}
