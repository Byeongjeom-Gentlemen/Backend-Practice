package com.sh.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @Schema(defaultValue = "ehftozl")
    @NotBlank(message = "아이디를 입력해주세요.")
    private String id;

    @Schema(defaultValue = "thdgus!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String pw;
}
