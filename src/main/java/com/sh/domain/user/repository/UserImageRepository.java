package com.sh.domain.user.repository;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.domain.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUser(User user);
}
