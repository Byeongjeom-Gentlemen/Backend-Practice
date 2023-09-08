package com.sh.domain.file.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileResponseDto {

    private String storeFileName;
    private String originalFileName;
    private String filePath;

    public static FileResponseDto of(String storeFileName, String originalFileName, String filePath) {
        return FileResponseDto.builder()
                .storeFileName(storeFileName)
                .originalFileName(originalFileName)
                .filePath(filePath)
                .build();
    }
}
