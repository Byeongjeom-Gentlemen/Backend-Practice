package com.sh.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserRequestDto {
    private String userId;

    @Pattern(regexp = "^[a-zA-Z]{4,10}$", message = "아이디는 4~10자 이내의 영문이여야 합니다.")
    private String afterId;

    @Pattern(regexp = "^[a-zA-Z]{6,}[^0-9a-zA-Zㄱ-ㅎ가-힣]{1}$", message = "비밀번호는 6자 이상의 영문으로만 조합하고 마지막에는 특수문자 1개를 포함해야 합니다.")
    private String afterPw;

    @Pattern(regexp = "^[가-힣]{2,4}$", message = "닉네임은 2~4자 이내의 한글이여야 합니다.")
    private String afterNickname;

    public void encryptPassword(String BCryptpassword) {
        this.afterPw = BCryptpassword;
    }
}
