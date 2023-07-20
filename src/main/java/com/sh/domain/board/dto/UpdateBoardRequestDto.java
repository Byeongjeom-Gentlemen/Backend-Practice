package com.sh.domain.board.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateBoardRequestDto {

    @NotBlank(message = "제목은 필수 입력 값 입니다.")
    @Size(min = 1, max = 10, message = "제목은 최소 1자리에서 최대 10자리 이내로 작성해주세요.")
    private String title;

    @NotNull private String content;
}
