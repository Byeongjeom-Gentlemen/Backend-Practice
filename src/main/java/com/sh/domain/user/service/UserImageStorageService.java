package com.sh.domain.user.service;

import com.sh.domain.file.dto.FileResponseDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface UserImageStorageService {

    // 회원 프로필 이미지 업로드
    @Transactional
    void uploadUserImg(MultipartFile file);
}
