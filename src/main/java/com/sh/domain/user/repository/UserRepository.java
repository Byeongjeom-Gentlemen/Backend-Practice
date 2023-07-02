package com.sh.domain.user.repository;

import com.sh.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/* @Repository */
public interface UserRepository extends JpaRepository<User, Long> {

    // 회원 생성
    User save(User user);

    // 아이디 중복 확인
    boolean existsByUserId(String id);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);
}
