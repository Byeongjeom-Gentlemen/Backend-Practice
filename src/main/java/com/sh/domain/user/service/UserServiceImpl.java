package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.*;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.UserErrorCode;
import com.sh.global.exception.customexcpetion.user.*;
import com.sh.global.util.SessionUtil;
import com.sh.global.util.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionUtil sessionUtil;

    /*
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder; */

    @Override
    public Long join(SignupRequestDto signupRequest) {
        // 아이디 중복확인
        existsByUserId(signupRequest.getId());

        // 닉네임 중복확인
        existsByNickname(signupRequest.getNickname());

        // 비밀번호 암호화
        signupRequest.encryptPassword(passwordEncoder.encode(signupRequest.getPw()));

        User user =
                User.builder()
                        .userId(signupRequest.getId())
                        .pw(signupRequest.getPw())
                        .nickname(signupRequest.getNickname())
                        .status(UserStatus.ALIVE)
                        .build();

        return userRepository.save(user).getId();
    }

    // 아이디 중복 확인
    private void existsByUserId(String userId) {
        if (userRepository.existsByUserId(userId)) {
            throw new AlreadyUsedUserIdException(UserErrorCode.ALREADY_EXISTS_ID);
        }
    }

    // 닉네임 중복 확인
    private void existsByNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new AlreadyUsedUserNicknameException(UserErrorCode.ALREADY_EXISTS_NICKNAME);
        }
    }

    // 로그인
    @Override
    public UserLoginResponseDto login(LoginRequestDto loginRequest) {
        /* // JWT
        // 1. id / pw를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userBasicRequestDto.getId(), userBasicRequestDto.getPw());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailsService의 loadUserByUsername 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 생성
        TokenDto token = jwtProvider.generateToken(authentication);

        User user = userRepository.findByUserId(userBasicRequestDto.getId())
        		.orElseThrow(() -> new UserNotFoundException());

        return UserLoginResponseDto.from(user, token); */

        // Session
        User user =
                userRepository
                        .findByUserId(loginRequest.getId())
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(loginRequest.getPw(), user.getPw())) {
            throw new NotMatchesUserException(UserErrorCode.INVALID_AUTHENTICATION);
        }

        if(user.getStatus() == UserStatus.WITHDRAWN) {
            throw new UserWithdrawalException(UserErrorCode.WITHDRAWN_USER);
        }

        sessionUtil.setAttribute(user.getId());

        return UserLoginResponseDto.from(user, "success");
    }

    // 내 정보 조회
    @Override
    public UserBasicResponseDto selectMe() {
        Long id = sessionUtil.getAttribute();

        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        if (user.getStatus() == UserStatus.WITHDRAWN) {
            throw new UserWithdrawalException(UserErrorCode.WITHDRAWN_USER);
        }

        return UserBasicResponseDto.from(user);
    }

    // 회원 삭제
    // CASCADE 옵션은 되도록 사용하지 않는다.
    @Override
    public void deleteUser() {
        Long id = sessionUtil.getAttribute();

        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        if (user.getStatus() == UserStatus.WITHDRAWN) {
            throw new UserWithdrawalException(UserErrorCode.WITHDRAWN_USER);
        }

        userRepository.delete(user);
        logout();
    }

    public void logout() {
        Long id = sessionUtil.getAttribute();

        userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        sessionUtil.invalidate();
    }

    // 회원 수정(PATCH)
    @Override
    public void modifyMe(UpdateUserRequestDto updateRequest) {
        Long id = sessionUtil.getAttribute();

        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        // 이미 탈퇴한 회원인 경우
        if (user.getStatus() == UserStatus.WITHDRAWN) {
            throw new UserWithdrawalException(UserErrorCode.WITHDRAWN_USER);
        }

        // 아이디 수정 시
        if (updateRequest.getAfterId() != null) {
            existsByUserId(updateRequest.getAfterId());
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
    public UserBasicResponseDto selectOtherUser(Long id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        if (user.getStatus() == UserStatus.WITHDRAWN) {
            throw new UserWithdrawalException(UserErrorCode.WITHDRAWN_USER);
        }

        return UserBasicResponseDto.from(user);
    }
}
