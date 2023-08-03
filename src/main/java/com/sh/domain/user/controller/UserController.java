package com.sh.domain.user.controller;

import com.sh.domain.user.dto.*;
import com.sh.domain.user.service.UserService;
import com.sh.global.util.jwt.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor // final 키워드가 붙은 필드에 자동으로 생성자 주입을 해주는 어노테이션
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    // @Valid : 자바에서 제공하는 유효성 검증 어노테이션, 유효성 검증과 관련된 어노테이션이 붙은 모든 필드를 검증, 유효성 검증에 실패하면
    // MethodArgumentNotValidException 발생
    // @Validated : 스프링에서 제공하는 유효성 검증 어노테이션, 그룹별 유효성 검증이 가능, Errors errors로 에러 여부 판단
    // 회원가입
    @Operation(
            summary = "회원가입 API",
            description = "회원을 생성하는 API 입니다. 회원가입 시에는 Id, Password, Nickname 값이 필요합니다.")
    @PostMapping("/api/v1/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Long join(@RequestBody @Valid SignupRequestDto signupRequest) {
        return userService.join(signupRequest);
    }

    // 로그인
    @Operation(summary = "로그인 API", description = "로그인하는 API 입니다. 로그인시에는 Id, Password 값이 필요합니다.")
    @PostMapping("/api/v1/users/login")
    public ResponseEntity<UserLoginResponseDto> login(
            @RequestBody @Valid LoginRequestDto loginRequest) {
        return ResponseEntity.ok().body(userService.login(loginRequest));
    }

    // 내 정보조회
    @Operation(summary = "내정보 조회 API", description = "내 정보를 조회하는 API 입니다. 로그인 여부를 필요로 합니다.")
    @GetMapping("/api/v1/users/me")
    public ResponseEntity<UserBasicResponseDto> myProfile() {
        return ResponseEntity.ok().body(userService.selectMe());
    }

    // 회원 삭제
    @Operation(summary = "회원삭제 API", description = "회원을 삭제하는 API 입니다. 회원삭제 시에는 로그인 여부를 필요로 합니다.")
    @DeleteMapping("/api/v1/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(HttpServletRequest request) {
        userService.deleteUser();

        // 회원삭제 후 로그아웃 처리
        logout(request);
    }

    // 회원 수정(PATCH)
    @Operation(
            summary = "회원수정 API",
            description = "회원정보를 수정하는 API 입니다. 로그인 여부를 필요로 하며, 수정필드 값이 존재할 경우 수정됩니다.")
    @PatchMapping("/api/v1/users/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modify(@RequestBody @Valid UpdateUserRequestDto updateRequest, HttpServletRequest request) {
        userService.modifyMe(updateRequest);
    }

    // 다른 회원 조회
    @Operation(
            summary = "다른회원 조회 API",
            description = "다른 회원정보를 조회하는 API 입니다. 회원의 ID(PK)값을 필요로 합니다.")
    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<UserBasicResponseDto> selectByOtherUser(@PathVariable Long userId) {
        return ResponseEntity.ok().body(userService.selectOtherUser(userId));
    }

    // 로그아웃
    @Operation(summary = "로그아웃 API", description = "회원 로그아웃하는 API 입니다. 로그인이 되어 있는 상태여야 합니다.")
    @GetMapping("/api/v1/users/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) {
        String accessToken = jwtProvider.resolveAccessToken(request);
        String refreshToken = jwtProvider.resolveRefreshToken(request);

        userService.logout(accessToken, refreshToken);
    }
}
