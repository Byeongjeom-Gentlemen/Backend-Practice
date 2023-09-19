package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.domain.UserImage;
import com.sh.domain.user.dto.request.SignupRequestDto;
import com.sh.domain.user.dto.request.UpdateUserRequestDto;
import com.sh.domain.user.dto.response.UserBasicResponseDto;
import com.sh.domain.user.repository.UserRepository;
import com.sh.domain.user.util.Role;
import com.sh.domain.user.util.UserStatus;
import com.sh.global.exception.customexcpetion.UserCustomException;
import com.sh.global.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String BASIC_IMAGE_NAME = "\\basic_profile_img.jpg";

    @Value("${custom.userImage-upload-path}")
    private String BASIC_IMAGE_PATH;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    @Override
    public Long join(SignupRequestDto signupRequest) {
        // 아이디 중복확인
        existsById(signupRequest.getId());

        // 닉네임 중복확인
        existsByNickname(signupRequest.getNickname());

        // 비밀번호 암호화
        signupRequest.encryptPassword(passwordEncoder.encode(signupRequest.getPw()));

        User user =
                User.builder()
                        .id(signupRequest.getId())
                        .pw(signupRequest.getPw())
                        .nickname(signupRequest.getNickname())
                        .role(Role.USER)
                        .status(UserStatus.ALIVE)
                        .build();

        UserImage image =
                UserImage.builder()
                        .user(user)
                        .imagePath(BASIC_IMAGE_PATH + BASIC_IMAGE_NAME)
                        .build();
        user.updateImage(image);

        return userRepository.save(user).getUserId();
    }

    // 아이디 중복 확인
    private void existsById(String id) {
        if (userRepository.existsById(id)) {
            throw UserCustomException.ALREADY_USED_USER_ID;
        }
    }

    // 닉네임 중복 확인
    private void existsByNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw UserCustomException.ALREADY_USED_USER_NICKNAME;
        }
    }

    // 내 정보 조회
    @Override
    public UserBasicResponseDto selectMe() {
        User user = getLoginUser();

        return UserBasicResponseDto.from(user);
    }

    // 회원 삭제
    @Override
    public void deleteUser() {
        User user = getLoginUser();
        userRepository.delete(user);
    }

    // 회원 수정(PATCH)
    @Override
    public void modifyMe(UpdateUserRequestDto updateRequest) {
        User user = getLoginUser();

        // 아이디 수정 시
        if (updateRequest.getAfterId() != null) {
            existsById(updateRequest.getAfterId());
            user.updateUserId(updateRequest.getAfterId());
        }

        // 닉네임 수정 시
        if (updateRequest.getAfterNickname() != null) {
            existsByNickname(updateRequest.getAfterNickname());
            user.updateUserNickname(updateRequest.getAfterNickname());
        }

        // 비밀번호 수정 시
        if (updateRequest.getAfterPw() != null) {
            String newPassword = passwordEncoder.encode(updateRequest.getAfterPw());
            user.updateUserPassword(newPassword);
        }
    }

    // 다른 회원 조회
    @Override
    public UserBasicResponseDto selectOtherUser(Long userId) {
        User user =
                userRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> UserCustomException.USER_NOT_FOUND);

        return UserBasicResponseDto.from(user);
    }

    // 로그인된 회원 정보 가져오기
    @Override
    public User getLoginUser() {
        String id = securityUtils.getCurrentUserId();

        User user = queryUser(id);
        user.verification();

        return user;
    }

    // 회원 조회
    private User queryUser(String id) {
        return userRepository.findById(id).orElseThrow(() -> UserCustomException.USER_NOT_FOUND);
    }
}
