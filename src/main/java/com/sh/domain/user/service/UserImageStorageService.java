package com.sh.domain.user.service;

import com.sh.domain.file.dto.FileResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface UserImageStorageService {

    // 회원 프로필 이미지 업로드
    @Transactional
    void uploadUserImg(MultipartFile file);

    // 회원 프로필 이미지 불러오기
    @Transactional(readOnly = true)
    byte[] showUserImg(Long userId);
}
