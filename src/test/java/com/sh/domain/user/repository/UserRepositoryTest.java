package com.sh.domain.user.repository;


import com.sh.domain.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    // 데이터 저장 테스트
    @Test
    @DisplayName("Data Save Test")
    public void saveTest() {
        // given
        User user = User.builder()
                .id("ehftozl").pw("thdgus!").nickname("할로")
                .build();

        // when
        userRepository.save(user);
        List<User> list = userRepository.findAll();
        User my = list.get(0);
        System.out.println(my.getId());
        System.out.println(my.getNickname());
        System.out.println(my.getCreatedDate());
        System.out.println(my.getModifiedDate());


        // then
        Assertions.assertThat(my.getId()).isEqualTo("ehftozl");
        Assertions.assertThat(my.getPw()).isEqualTo("thdgus!");
        Assertions.assertThat(my.getNickname()).isEqualTo("할로");
    }

}