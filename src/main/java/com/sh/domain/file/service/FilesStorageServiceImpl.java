package com.sh.domain.file.service;

import com.sh.domain.file.dto.FileResponseDto;
import com.sh.global.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FilesStorageServiceImpl implements FilesStorageService {

    private final FileUtils fileUtils;

    // 이미지 파일 업로드
    @Override
    public FileResponseDto uploadImg(String uploadPath, MultipartFile file) {
        // 디렉토리 체크 및 생성
        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (InputStream inputStream = file.getInputStream()) {
            // 이미지 파일 변조 체크
            fileUtils.validImgFile(inputStream);

            // 서버에 저장할 파일 이름 설정
            // 랜덤 고유번호로 파일명을 변경해 저장하여 파일명 중복을 방지함. (파일 덮어쓰기 방지)
            String storeFileName = UUID.randomUUID() + "." + extractExt(file.getOriginalFilename());
            // 이미지 파일 저장 경로
            String saveFilePath = uploadPath + File.separator + storeFileName;

            // 파일 업로드
            file.transferTo(Paths.get(saveFilePath));

            return FileResponseDto.of(storeFileName, file.getOriginalFilename(), saveFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
