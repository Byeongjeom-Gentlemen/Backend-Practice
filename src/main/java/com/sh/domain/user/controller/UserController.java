package com.sh.domain.user.controller;

import com.sh.domain.user.dto.request.SignupRequestDto;
import com.sh.domain.user.dto.request.UpdateUserRequestDto;
import com.sh.domain.user.dto.response.UserBasicResponseDto;
import com.sh.domain.user.service.AuthService;
import com.sh.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor // final 키워드가 붙은 필드에 자동으로 생성자 주입을 해주는 어노테이션
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

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

    // 내 정보조회
    @Operation(summary = "내정보 조회 API", description = "내 정보를 조회하는 API 입니다. 로그인 여부를 필요로 합니다.")
    @GetMapping("/api/v1/users/me")
    @ResponseStatus(HttpStatus.OK)
    public UserBasicResponseDto myProfile() {
        return userService.selectMe();
    }

    // 회원 삭제
    @Operation(summary = "회원삭제 API", description = "회원을 삭제하는 API 입니다. 회원삭제 시에는 로그인 여부를 필요로 합니다.")
    @DeleteMapping("/api/v1/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        // 로그아웃
        authService.logout();

        userService.deleteUser();
    }

    // 회원 수정(PATCH)
    @Operation(
            summary = "회원수정 API",
            description = "회원정보를 수정하는 API 입니다. 로그인 여부를 필요로 하며, 수정필드 값이 존재할 경우 수정됩니다.")
    @PatchMapping("/api/v1/users/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modify(@RequestBody @Valid UpdateUserRequestDto updateRequest) {
        userService.modifyMe(updateRequest);

        // 로그아웃
        authService.logout();
    }

    // 다른 회원 조회
    @Operation(
            summary = "다른회원 조회 API",
            description = "다른 회원정보를 조회하는 API 입니다. 회원의 ID(PK)값을 필요로 합니다.")
    @GetMapping("/api/v1/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserBasicResponseDto selectByOtherUser(@PathVariable Long userId) {
        return userService.selectOtherUser(userId);
    }
}
