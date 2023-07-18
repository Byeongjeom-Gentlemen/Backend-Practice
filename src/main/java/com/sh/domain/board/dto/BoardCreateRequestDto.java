package com.sh.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardCreateRequestDto {

    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣0-9\\s!*~^%$#@(]{1,10}$", message = "제목은 최소 1자, 최대 10자 사이로 설정해주세요.")
    private String title;

    private String content;
}
