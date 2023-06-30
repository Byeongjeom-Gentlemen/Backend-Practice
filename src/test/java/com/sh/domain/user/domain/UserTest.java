package com.sh.domain.user.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("createUser test")
    public void createUser() {

        User user = User.builder().id("ehftozl").pw("thdgus!").nickname("할로").build();

        Assertions.assertThat(user.getId()).isEqualTo("ehftozl");
        Assertions.assertThat(user.getPw()).isEqualTo("thdgus!");
        Assertions.assertThat(user.getNickname()).isEqualTo("할로");
    }
}