package com.sh.domain.user.controller;

import com.sh.domain.user.dto.*;
import com.sh.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor    // final 키워드가 붙은 필드에 자동으로 생성자 주입을 해주는 어노테이션
public class UserController {

    private final UserService userService;

    // @Valid : 자바에서 제공하는 유효성 검증 어노테이션, 유효성 검증과 관련된 어노테이션이 붙은 모든 필드를 검증, 유효성 검증에 실패하면 MethodArgumentNotValidException 발생
    // @Validated : 스프링에서 제공하는 유효성 검증 어노테이션, 그룹별 유효성 검증이 가능, Errors errors로 에러 여부 판단
    // 회원가입
    @PostMapping("/api/v1/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Long join(@RequestBody @Valid SignupRequestDto signupRequest){
        return userService.join(signupRequest);
    }

    // 로그인
    @PostMapping("/api/v1/users/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequest){
        return ResponseEntity.ok().body(userService.login(loginRequest));
    }

    // 내 정보 조회
    @GetMapping("/api/v1/users/me")
    public ResponseEntity<UserBasicResponseDto> myProfile() {
        return ResponseEntity.ok().body(userService.selectMe());
    }

    // 회원 삭제
    @Operation(summary = "회원 삭제", description = "회원을 삭제하는 API입니다. 회원 삭제 시에는 userId가 필요합니다")
    @DeleteMapping("/api/v1/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        userService.deleteUser();
    }

    // 회원 수정(PATCH)
    @PatchMapping("/api/v1/users/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modify(@RequestBody @Valid UpdateUserRequestDto updateRequest) {
        userService.modifyMe(updateRequest);
    }

    // 다른 회원 조회
    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<UserBasicResponseDto> selectByOtherUser(@PathVariable Long id) {
        return ResponseEntity.ok()
                .body(userService.selectOtherUser(id));
    }

}
