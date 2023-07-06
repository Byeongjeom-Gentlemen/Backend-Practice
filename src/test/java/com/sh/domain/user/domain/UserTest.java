package com.sh.domain.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    @DisplayName("유저 생성 테스트")
    public void createUser() {

        // given

        // when
        User user = User.builder().userId("ehftozl").pw("thdgus!").nickname("할로").build();

        // then
        Assertions.assertThat(user.getUserId()).isEqualTo("ehftozl");
        Assertions.assertThat(user.getPw()).isEqualTo("thdgus!");
        Assertions.assertThat(user.getNickname()).isEqualTo("할로");
    }
}