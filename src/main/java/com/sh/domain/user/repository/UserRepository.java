package com.sh.domain.user.repository;

import com.sh.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 회원 생성
    User save(User user);

    // 아이디 중복 확인
    boolean existsByUserId(String userId);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);
    
    // 유저 아이디로 pk값 가져오기
    Long getById(String userId);

    // 유저 아이디로 회원 정보 찾기
    Optional<User> findByUserId(String userId);

    // 유저 닉네임으로 회원 정보 찾기
    Optional<User> findByNickname(String nickname);

    // 회원 삭제
    void delete(User user);
}
