package com.sh.global.util.file;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileResponseDto {

    private String storeFileName;
    private String originalFileName;
    private String filePath;
    private String fileType;
    private long fileSize;

    public static FileResponseDto of(
            String storeFileName,
            String originalFileName,
            String filePath,
            String fileType,
            long fileSize) {
        return FileResponseDto.builder()
                .storeFileName(storeFileName)
                .originalFileName(originalFileName)
                .filePath(filePath)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }
}
