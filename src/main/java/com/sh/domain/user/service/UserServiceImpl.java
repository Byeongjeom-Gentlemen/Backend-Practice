package com.sh.domain.user.service;

import com.sh.domain.user.domain.RefreshToken;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.*;
import com.sh.domain.user.repository.UserRepository;
import com.sh.domain.user.util.Role;
import com.sh.domain.user.util.UserStatus;
import com.sh.global.exception.customexcpetion.token.NonTokenException;
import com.sh.global.exception.customexcpetion.user.*;
import com.sh.global.exception.errorcode.TokenErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.util.CustomUserDetails;
import com.sh.global.util.SecurityUtils;
import com.sh.global.util.jwt.JwtProvider;
import com.sh.global.util.jwt.TokenDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRedisService userRedisService;

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

        return userRepository.save(user).getUserId();
    }

    // 아이디 중복 확인
    private void existsById(String id) {
        if (userRepository.existsById(id)) {
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
        try {
            // id / pw를 기반으로 Authentication 객체 생성 및 실제 검증 (아이디 존재 여부, 사용자 비밀번호 체크)
            // 아이디가 존재하지 않거나 id와 비밀번호가 맞지 않으면 예외처리
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getId(), loginRequest.getPw()));

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // 인증정보를 기반으로 토큰 생성
            String accessToken = jwtProvider.generateAccessToken(customUserDetails);
            String refreshToken = jwtProvider.generateRefreshToken();
            
            // Redis에 저장 (access token과 refresh token 값을 유저와 1대1로 저장)
            userRedisService.saveRefreshToken(customUserDetails.getUsername(), refreshToken, accessToken);

            // 토큰정보
            TokenDto token = TokenDto.of(accessToken, refreshToken);

            return UserLoginResponseDto.from(customUserDetails, token);

        } catch (BadCredentialsException e) {
            throw new NotMatchesUserException(UserErrorCode.INVALID_AUTHENTICATION);
        }
    }

    // 내 정보 조회
    @Override
    public UserBasicResponseDto selectMe() {
        String id = securityUtils.getCurrentUserId();
        User user = getUser(id);

        return UserBasicResponseDto.from(user);
    }

    // 회원 삭제
    // CASCADE 옵션은 되도록 사용하지 않는다.
    @Override
    public void deleteUser() {
        String id = securityUtils.getCurrentUserId();
        User user = getUser(id);

        userRepository.delete(user);
    }

    // 로그아웃
    @Override
    public void logout(String accessToken) {
        if (accessToken == null) {
            throw new NonTokenException(TokenErrorCode.NON_ACCESS_TOKEN_REQUEST_HEADER);
        }

        // Refresh Token 조회
        RefreshToken refreshToken = userRedisService.selectRefreshToken(accessToken);

        // Refresh Token 삭제
        userRedisService.deleteRefreshToken(refreshToken);

        // BlackList Token에 해당 Access Token 저장
        userRedisService.saveBlackListToken(accessToken);
    }

    // 회원 수정(PATCH)
    @Override
    public void modifyMe(UpdateUserRequestDto updateRequest) {
        String id = securityUtils.getCurrentUserId();
        User user = getUser(id);

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

    // 회원 ID로 유저정보 가져오기
    private User getUser(String id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        if (user.getStatus() == UserStatus.WITHDRAWN) {
            throw new UserWithdrawalException(UserErrorCode.WITHDRAWN_USER);
        }

        return user;
    }

    // 다른 회원 조회
    @Override
    public UserBasicResponseDto selectOtherUser(Long userId) {
        User user =
                userRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        return UserBasicResponseDto.from(user);
    }
}
