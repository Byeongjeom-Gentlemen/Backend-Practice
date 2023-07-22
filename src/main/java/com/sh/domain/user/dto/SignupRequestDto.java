package com.sh.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 생성
@NoArgsConstructor // 기본 생성자 생성
@Getter
@Setter
@Builder
public class SignupRequestDto {
    @Schema(defaultValue = "ehftozl")
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-zA-Z]{4,10}$", message = "아이디는 4~10자 이내의 영문이여야 합니다.")
    private String id;

    @Schema(defaultValue = "thdgus!")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(
            regexp = "^[a-zA-Z]{6,}[^0-9a-zA-Zㄱ-ㅎ가-힣]{1}$",
            message = "비밀번호는 6자 이상의 영문으로만 조합하고 마지막에는 특수문자 1개를 포함해야 합니다.")
    private String pw;

    @Schema(defaultValue = "헬로우")
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣]{2,4}$", message = "닉네임은 2~4자 이내의 한글이여야 합니다.")
    private String nickname;

    public void encryptPassword(String BCryptpassword) {
        this.pw = BCryptpassword;
    }
}
