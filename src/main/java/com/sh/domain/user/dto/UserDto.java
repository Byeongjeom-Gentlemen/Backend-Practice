package com.sh.domain.user.dto;

import com.sh.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 생성
@NoArgsConstructor  // 기본 생성자 생성
@Getter
@Setter
public class UserDto {
    @NotBlank(message = "아이디를 입력해주세요")
    @Pattern(regexp = "/^[a-zA-Z]{4,10}$/", message = "아이디는 4~10자 이내의 영문이여야 합니다.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "/^[a-zA-Z]{6,}[^0-9a-zA-Zㄱ-ㅎ가-힣]{1}$/", message = "비밀번호는 6자 이상의 영문이여야 합니다. 마지막에는 특수문자 1개를 포함해야 합니다.")
    private String pw;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "/^[가-힣]{2,4}$/", message = "닉네임은 2~4자 이내의 한글이여야 합니다.")
    private String nickname;

    public void encryptPassword(String BCryptpassword) {
        this.pw = BCryptpassword;
    }

    public User toEntity() {
        User user = User.builder()
                .userId(id)
                .pw(pw)
                .nickname(nickname)
                .build();

        return user;
    }
}
