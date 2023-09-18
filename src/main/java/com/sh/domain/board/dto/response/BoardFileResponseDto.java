package com.sh.domain.board.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardFileResponseDto {

    private Long fileId;
    private String storeName;
    private String originalName;
    private String filePath;
    private String fileType;

    public static BoardFileResponseDto of(
            Long fileId, String storeName, String originalName, String filePath, String fileType) {
        return BoardFileResponseDto.builder()
                .fileId(fileId)
                .storeName(storeName)
                .originalName(originalName)
                .filePath(filePath)
                .fileType(fileType)
                .build();
    }
}
