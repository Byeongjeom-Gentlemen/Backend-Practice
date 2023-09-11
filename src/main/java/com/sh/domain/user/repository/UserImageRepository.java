package com.sh.domain.user.repository;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.domain.UserImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUser(User user);
}
