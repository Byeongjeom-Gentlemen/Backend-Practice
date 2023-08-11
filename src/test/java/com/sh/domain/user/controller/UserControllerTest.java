package com.sh.domain.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// @WebMvcTest(UserController.class)
// 통합테스트
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    /*
    @Autowired ObjectMapper mapper;

    @Autowired MockMvc mvc;

    @Test
    @DisplayName("회원가입 성공 시")
    public void joinVer01() throws Exception {
        // given
        String id = "ehftozl";
        String pw = "thdgus!";
        String nickname = "헬로";

        // when
        String body =
                mapper.writeValueAsString(
                        User.builder().userId(id).pw(pw).nickname(nickname).build());

        // then
        mvc.perform(post("/api/v1/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("회원가입 실패 - 유효성 검증 실패")
    public void joinVer02() throws Exception {
        // given
        String id = "ehftozl";
        String pw = "thdgusthdgus";
        String nickname = "헬로";

        // when
        String body =
                mapper.writeValueAsString(
                        User.builder().userId(id).pw(pw).nickname(nickname).build());

        // then
        mvc.perform(post("/api/v1/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("회원가입 실패 - 아이디 혹은 닉네임 중복")
    public void joinVer03() throws Exception {
        // given
        String id = "ehftozl";
        String pw = "thdgus!";
        String nickname = "헬로";

        // when
        String body =
                mapper.writeValueAsString(
                        User.builder().userId(id).pw(pw).nickname(nickname).build());

        // then
        mvc.perform(post("/api/v1/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        // given
        String id02 = "saradn";
        String pw02 = "thdgus!";
        String nickname02 = "헬로";

        // when
        String body02 =
                mapper.writeValueAsString(
                        User.builder().userId(id02).pw(pw02).nickname(nickname02).build());

        // then
        mvc.perform(post("/api/v1/users").content(body02).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409));
    }

     */
}
