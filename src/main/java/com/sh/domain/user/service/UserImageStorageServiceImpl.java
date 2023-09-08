package com.sh.domain.user.service;

import com.sh.domain.file.dto.FileResponseDto;
import com.sh.domain.file.service.FilesStorageService;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.domain.UserImage;
import com.sh.domain.user.repository.UserImageRepository;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.customexcpetion.FileCustomException;
import com.sh.global.exception.customexcpetion.UserCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
        if(file.isEmpty()) {
            throw new RuntimeException();
        }

        User user = userService.getLoginUser();

        // 이미지 업로드
        FileResponseDto saveFile = filesStorageService.uploadImg(uploadPath, file);
        if(saveFile == null) {
            throw new RuntimeException();
        }

        UserImage userImage = UserImage.builder()
                .user(user)
                .storeName(saveFile.getStoreFileName())
                .originalName(saveFile.getOriginalFileName())
                .imagePath(saveFile.getFilePath())
                .build();

        // 이미지 수정
        user.updateImage(userImage);
        imageRepository.save(userImage);
    }
}
