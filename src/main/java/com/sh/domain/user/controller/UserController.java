package com.sh.domain.user.controller;

import com.sh.domain.user.dto.*;
import com.sh.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Long> join(@RequestBody @Valid SignupRequestDto signupRequestDto){
        return ResponseEntity.created(URI.create("/api/v1/users")).body(userService.join(signupRequestDto));
    }

    // 로그인
    @PostMapping("/api/v1/users/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserBasicRequestDto userBasicRequestDto, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok().body(userService.login(userBasicRequestDto, httpServletRequest));
    }

    // 내 정보 조회
    @GetMapping("/api/v1/users/me")
    // controller에서 요청을 수행하기 전 Filter가 요청 헤더에 대한 검증작업 수행(JwtAuthenticationFilter)
    public ResponseEntity<UserBasicResponseDto> myProfile(@SessionAttribute(name = "userId", required = false) String userId) {
        return ResponseEntity.ok().body(userService.selectMe(userId));
    }


    // 회원 삭제
    @DeleteMapping("/api/v1/users")
    public ResponseEntity<UserBasicResponseDto> deleteUser(@RequestBody @Valid UserBasicRequestDto userBasicRequestDto,
                                                           @SessionAttribute(name = "userId", required = false) String userId) {
        userService.deleteUser(userBasicRequestDto, userId);
        return ResponseEntity.noContent().build();
    }

    // 회원 수정(PATCH)
    @PatchMapping("/api/v1/users/me")
    public ResponseEntity<UserBasicResponseDto> modify(@RequestBody @Valid UpdateUserRequestDto user,
                                                       @SessionAttribute(name = "userId", required = false) String userId) {
        userService.modifyMe(user, userId);
        return ResponseEntity.noContent().build();
    }

    // 다른 회원 조회
    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<UserBasicResponseDto> selectByOtherUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(userService.selectOtherUser(id));
    }

}
