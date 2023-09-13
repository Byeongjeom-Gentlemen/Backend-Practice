package com.sh.domain.user.service;

import com.sh.global.util.file.FileResponseDto;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.domain.UserImage;
import com.sh.domain.user.repository.UserImageRepository;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.customexcpetion.FileCustomException;
import com.sh.global.exception.customexcpetion.UserCustomException;
import java.io.*;
import javax.persistence.PrePersist;

import com.sh.global.util.file.FileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserImageStorageServiceImpl implements UserImageStorageService {

    private static final String BASIC_USER_IMAGE_NAME = "basic_profile_img.jpg";

    @Value("${custom.userImage-upload-path}")
    private String uploadPath;

    private final FileUtils fileUtils;
    private final UserService userService;
    private final UserImageRepository imageRepository;
    private final UserRepository userRepository;

    // 회원 프로필 이미지 업로드
    @Override
    public void uploadUserImg(MultipartFile file) {
        if (file.isEmpty()) {
            throw FileCustomException.FILE_DOES_NOT_EXIST_REQUEST_VALUE;
        }

        // 로그인한 회원의 정보
        User user = userService.getLoginUser();
        // 회원의 이미지 정보
        UserImage image = user.getImage();

        // 기본 이미지가 아니라면, 회원의 이미지 삭제
        if (image.getStoreName() != null) {
            fileUtils.deleteFile(image.getImagePath());
        }

        // 이미지파일 변조 체크
        fileUtils.validImgFile(file);

        // 경로에 이미지 업로드
        FileResponseDto saveFile = fileUtils.uploadFile(uploadPath, file);
        if (saveFile == null) {
            throw FileCustomException.FAILED_UPLOAD_FILE;
        }

        // 새로운 이미지 정보로 update
        image.updateImage(
                saveFile.getStoreFileName(),
                saveFile.getOriginalFileName(),
                saveFile.getFilePath());
        // 회원의 이미지 정보 update
        user.updateImage(image);
    }

    // 회원 프로필 이미지 불러오기
    @Override
    public byte[] showUserImg(Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> UserCustomException.USER_NOT_FOUND);

        UserImage image = user.getImage();
        byte[] result = null;

        try (InputStream imageStream = new FileInputStream(image.getImagePath())) {
            result = IOUtils.toByteArray(imageStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // 회원 프로필 이미지 삭제(기본 이미지로 변경)
    @Override
    public void deleteImg() {
        User user = userService.getLoginUser();

        UserImage image =
                imageRepository
                        .findByUser(user)
                        .orElseThrow(() -> FileCustomException.FILE_DOES_NOT_EXIST);

        // 경로에서 이미지파일 삭제
        fileUtils.deleteFile(image.getImagePath());

        // 기본 이미지 정보 저장
        image.updateBasicImage(uploadPath + BASIC_USER_IMAGE_NAME);

        // 회원 이미지 정보 저장
        user.updateImage(image);
    }
}
