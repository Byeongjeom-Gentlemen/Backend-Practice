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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor    // final 키워드가 붙은 필드에 자동으로 생성자 주입을 해주는 어노테이션
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/api/v1/users")
    public ResponseEntity<? extends BasicResponse> join(@RequestBody @Valid UserDto userDto) {
        Long id = userServiceImpl.join(userDto);
        if(id == Long.MIN_VALUE) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("아이디 또는 닉네임이 이미 사용중입니다.", "409"));
        }

        return ResponseEntity.ok().body(new CommonResponse<>(id));
    }

}
