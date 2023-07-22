package com.sh.domain.board.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchRequestDto {

    @NotBlank private String searchType;

    @NotBlank private String keyword;
}
