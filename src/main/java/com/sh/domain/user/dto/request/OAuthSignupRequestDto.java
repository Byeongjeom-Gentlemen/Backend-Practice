package com.sh.domain.user.dto.request;

import com.sh.global.oauth.OAuthProvider;
import com.sh.global.oauth.kakao.KakaoLoginParams;
import lombok.Getter;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class OAuthSignupRequestDto {

    @NotBlank
    private String oauthProviderId;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(min = 2, max = 4)
    @Pattern(regexp = "^[가-힣]{2,4}$", message = "닉네임은 2~4자 이내의 한글이여야 합니다.")
    private String nickname;
}
