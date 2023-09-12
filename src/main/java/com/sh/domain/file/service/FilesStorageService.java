package com.sh.domain.file.service;

import com.sh.domain.file.dto.FileResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

    // 이미지 파일 업로드
    FileResponseDto uploadImg(String uploadPath, MultipartFile file);

    // 업로드한 파일 삭제
    void deleteFile(String uploadPath);
}