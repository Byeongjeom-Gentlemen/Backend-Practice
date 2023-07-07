package com.sh.domain.user.controller;

import com.sh.domain.user.dto.LoginDto;
import com.sh.domain.user.dto.UserRequestDto;
import com.sh.domain.user.dto.UserResponseDto;
import com.sh.domain.user.service.UserService;
import jdk.jshell.spi.ExecutionControlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor    // final 키워드가 붙은 필드에 자동으로 생성자 주입을 해주는 어노테이션
public class UserController {

    private final UserService userService;

    // @Valid : 자바에서 제공하는 유효성 검증 어노테이션, 유효성 검증과 관련된 어노테이션이 붙은 모든 필드를 검증, 유효성 검증에 실패하면 MethodArgumentNotValidException 발생
    // @Validated : 스프링에서 제공하는 유효성 검증 어노테이션, 그룹별 유효성 검증이 가능, Errors errors로 에러 여부 판단
    // 회원가입
    @PostMapping("/api/v1/users")
    public ResponseEntity<Long> join(@RequestBody @Valid UserRequestDto userRequestDto) throws Exception {
        return ResponseEntity.created(URI.create("/api/v1/users")).body(userService.join(userRequestDto));
    }

    // 로그인
    @PostMapping("/api/v1/users/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody @Valid LoginDto loginDto) throws Exception {
        return ResponseEntity.ok().body(userService.login(loginDto));
    }

    // 내 정보 조회
    @GetMapping("/api/v1/users/me")
    public ResponseEntity<?> myProfile(HttpServletRequest requset) {
        return ResponseEntity.ok().body(null);
    }

}
