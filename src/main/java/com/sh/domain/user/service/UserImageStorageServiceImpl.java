package com.sh.domain.user.service;

import com.sh.domain.file.dto.FileResponseDto;
import com.sh.domain.file.service.FilesStorageService;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.domain.UserImage;
import com.sh.domain.user.repository.UserImageRepository;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.customexcpetion.FileCustomException;
import com.sh.global.exception.customexcpetion.UserCustomException;
import java.io.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserImageStorageServiceImpl implements UserImageStorageService {

    @Value("${custom.userImage-upload-path}")
    private String uploadPath;

    private final UserService userService;
    private final FilesStorageService filesStorageService;
    private final UserImageRepository imageRepository;
    private final UserRepository userRepository;

    // 회원 프로필 이미지 업로드
    @Override
    public void uploadUserImg(MultipartFile file) {
        if (file.isEmpty()) {
            throw FileCustomException.FILE_DOES_NOT_EXIST_REQUEST_VALUE;
        }

        User user = userService.getLoginUser();

        // 이미지 업로드
        FileResponseDto saveFile = filesStorageService.uploadImg(uploadPath, file);
        if (saveFile == null) {
            throw FileCustomException.FAILED_UPLOAD_FILE;
        }

        UserImage userImage =
                UserImage.builder()
                        .user(user)
                        .storeName(saveFile.getStoreFileName())
                        .originalName(saveFile.getOriginalFileName())
                        .imagePath(saveFile.getFilePath())
                        .build();

        // 이미지 수정
        user.updateImage(userImage);
        imageRepository.save(userImage);
    }

    // 회원 프로필 이미지 불러오기
    @Override
    public byte[] showUserImg(Long userId) {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> UserCustomException.USER_NOT_FOUND);

        // 기본 이미지 경로
        String imagePath = uploadPath + File.separator + "basic_profile_img.jpg";

        UserImage image = imageRepository.findByUser(user).orElse(null);
        // 이미지 정보가 존재한다면 해당 이미지 정보 path 저장
        // 존재하지 않으면 기본 이미지 정보 저장
        if (image != null) {
            imagePath = image.getImagePath();
        }

        byte[] result = null;

        try (InputStream imageStream = new FileInputStream(imagePath)) {
            result = IOUtils.toByteArray(imageStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
