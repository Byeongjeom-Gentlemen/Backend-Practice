package com.sh.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBoardRequestDto {

    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    @Size(min = 1, max = 10, message = "제목은 최소 1자, 최대 10자 사이로 설정해주세요.")
    private String title;

    private String content;
}
