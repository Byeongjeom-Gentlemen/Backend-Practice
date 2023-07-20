package com.sh.domain.user.repository;

import com.sh.domain.user.domain.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // 회원 생성
    User save(User user);

    // 아이디 중복 확인
    boolean existsByUserId(String userId);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);

    // 유저 번호로 회원 정보 찾기
    Optional<User> findById(Long id);

    // 유저 아이디로 회원 정보 찾기
    Optional<User> findByUserId(String userId);

    // 회원 삭제
    void delete(User user);

}
