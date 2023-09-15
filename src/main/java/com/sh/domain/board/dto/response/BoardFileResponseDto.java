package com.sh.domain.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardFileResponseDto {

    private String storeName;
    private String originalName;
    private String filePath;
    private String fileType;

    public static BoardFileResponseDto of(String storeName, String originalName, String filePath, String fileType) {
        return BoardFileResponseDto.builder()
                .storeName(storeName)
                .originalName(originalName)
                .filePath(filePath)
                .fileType(fileType)
                .build();
    }
}
