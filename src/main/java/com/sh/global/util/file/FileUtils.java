package com.sh.global.util.file;

import com.sh.global.exception.customexcpetion.FileCustomException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtils {

    private static final Tika tika = new Tika();

    // 이미지 파일 확장자 변조 체크
    public void validImgFile(MultipartFile file) {
        try(InputStream imageStream = file.getInputStream()) {
            List<String> notValidTypeList =
                    Arrays.asList(
                            "image/jpeg",
                            "image/pjpeg",
                            "image/png",
                            "image/gif",
                            "image/bmp",
                            "image/x-windows-bmp");

            String mimeType = tika.detect(imageStream);
            boolean isValid =
                    notValidTypeList.stream()
                            .anyMatch(notValidType -> notValidType.equalsIgnoreCase(mimeType));

            if (!isValid) {
                throw FileCustomException.IS_NOT_IMAGE;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 단일 파일 업로드
    public FileResponseDto uploadFile(String uploadPath, MultipartFile file) {
        // 디렉토리 체크 및 생성
        File directory = new File(uploadPath);
        if(!directory.exists()) {
            directory.mkdirs();
        }
        
        // 서버에 저장할 파일 이름 설정
        // 랜덤 고유번호로 파일명을 변경해 저장하여 파일명 중복을 방지함. (파일 덮어쓰기 방지)
        String storeFileName = UUID.randomUUID() + "." + extractExt(file.getOriginalFilename());
        // 이미지 파일 저장 경로
        String saveFilePath = uploadPath + File.separator + storeFileName;

        try {
            // 파일 업로드
            file.transferTo(Paths.get(saveFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FileResponseDto.of(storeFileName, file.getOriginalFilename(), saveFilePath, file.getContentType(), file.getSize());
    }

    // 다중 파일 업로드
    public List<FileResponseDto> uploadFiles(String uploadPath, List<MultipartFile> files) {
        List<FileResponseDto> fileList = new ArrayList<>();

        for(MultipartFile file : files) {
            if(file.isEmpty()) {
                continue;
            }
            fileList.add(uploadFile(uploadPath, file));
        }

        return fileList;
    }

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
    
    // 파일 삭제
    public void deleteFile(String uploadPath) {
        File file = new File(uploadPath);
        file.delete();
    }
}
