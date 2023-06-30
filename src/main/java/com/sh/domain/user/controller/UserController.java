package com.sh.domain.user.controller;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.UserDto;
import com.sh.domain.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor    // final 키워드가 붙은 필드에 자동으로 생성자 주입을 해주는 어노테이션
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/api/v1/users")
    public void join(@RequestBody UserDto userDto, Errors errors) {
        if(errors.hasErrors()) {

        }
    }

}
