package com.sh.domain.user.controller;

import com.sh.domain.user.dto.LoginDto;
import com.sh.domain.user.dto.UserDto;
import com.sh.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor    // final 키워드가 붙은 필드에 자동으로 생성자 주입을 해주는 어노테이션
public class UserController {

    private final UserService userService;

    // @Valid : 자바에서 제공하는 유효성 검증 어노테이션, 유효성 검증과 관련된 어노테이션이 붙은 모든 필드를 검증, 유효성 검증에 실패하면 MethodArgumentNotValidException 발생
    // @Validated : 스프링에서 제공하는 유효성 검증 어노테이션, 그룹별 유효성 검증이 가능, Errors errors로 에러 여부 판단
    // 회원가입
    @PostMapping("/api/v1/users")
    public ResponseEntity<?> join(@RequestBody @Valid UserDto userDto) {
        return userService.join(userDto);
    }

    // 로그인
    @PostMapping("/api/v1/users/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        return userService.login(loginDto);
    }

}
