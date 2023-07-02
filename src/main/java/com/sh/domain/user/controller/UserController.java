package com.sh.domain.user.controller;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.UserDto;
import com.sh.domain.user.service.UserServiceImpl;
import com.sh.global.common.response.BasicResponse;
import com.sh.global.common.response.CommonResponse;
import com.sh.global.common.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor    // final 키워드가 붙은 필드에 자동으로 생성자 주입을 해주는 어노테이션
public class UserController {

    private final UserServiceImpl userServiceImpl;

    // @Valid : 자바에서 제공하는 유효성 검증 어노테이션, 유효성 검증과 관련된 어노테이션이 붙은 모든 필드를 검증, 유효성 검증에 실패하면 MethodArgumentNotValidException 발생
    // @Validated : 스프링에서 제공하는 유효성 검증 어노테이션, 그룹별 유효성 검증이 가능, Errors errors로 에러 여부 판단
    @PostMapping("/api/v1/users")
    public ResponseEntity<? extends BasicResponse> join(@RequestBody @Valid UserDto userDto) {
        System.out.println(userDto.getId());

        Long id = userServiceImpl.join(userDto);
        System.out.println(id);
        if(id == Long.MIN_VALUE) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("아이디 혹은 닉네임이 이미 사용중입니다.", "409"));
        }
        return ResponseEntity.ok().body(new CommonResponse<>(id));
    }

}
