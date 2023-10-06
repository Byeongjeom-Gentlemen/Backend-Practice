package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.domain.UserImage;
import com.sh.domain.user.dto.request.OAuthSignupRequestDto;
import com.sh.domain.user.dto.response.OAuthLoginResponseDto;
import com.sh.domain.user.repository.RefreshTokenRedisRepository;
import com.sh.domain.user.repository.UserRepository;
import com.sh.domain.user.util.Role;
import com.sh.domain.user.util.UserStatus;
import com.sh.global.exception.customexcpetion.OAuthCustomException;
import com.sh.global.exception.customexcpetion.UserCustomException;
import com.sh.global.oauth.OAuthInfoResponse;
import com.sh.global.oauth.OAuthLoginParams;
import com.sh.global.oauth.OAuthProvider;
import com.sh.global.oauth.RequestOAuthInfoService;
import com.sh.global.util.CustomUserDetails;
import com.sh.global.util.jwt.JwtProvider;
import com.sh.global.util.jwt.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final String BASIC_IMAGE_NAME = "\\basic_profile_img.jpg";

    @Value("${custom.userImage-upload-path}")
    private String imagePath;

    private final UserRepository userRepository;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final UserRedisService userRedisService;
    private final AuthService authService;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    // OAuth 로그인
    public OAuthLoginResponseDto oauthLogin(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);

        return isLoginCheck(oAuthInfoResponse);
    }

    // 로그인 체크
    private OAuthLoginResponseDto isLoginCheck(OAuthInfoResponse oAuthInfoResponse) {
        User user =
                userRepository
                        .findByProviderAndProviderId(
                                oAuthInfoResponse.getOAuthProvider(),
                                oAuthInfoResponse.getOAuthProviderId())
                        .orElse(null);

        if (user != null) {
            return isLoginOk(user, oAuthInfoResponse);
        }

        return isLoginFail(oAuthInfoResponse);
    }

    // 로그인 성공, jwt 토큰 발급
    private OAuthLoginResponseDto isLoginOk(User user, OAuthInfoResponse oAuthInfoResponse) {
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        // 인증정보를 기반으로 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(customUserDetails);
        String refreshToken = jwtProvider.generateRefreshToken();

        // Redis에 저장 (access token과 refresh token 값을 유저와 1대1로 저장)
        userRedisService.saveRefreshToken(
                customUserDetails.getUsername(), refreshToken, accessToken);

        // 토큰정보
        TokenDto token = TokenDto.of(accessToken, refreshToken);

        return OAuthLoginResponseDto.of(true, oAuthInfoResponse, user.getNickname(), token);
    }

    // 로그인 실패, 회원가입 필요
    private OAuthLoginResponseDto isLoginFail(OAuthInfoResponse oAuthInfoResponse) {
        return OAuthLoginResponseDto.of(false, oAuthInfoResponse, null);
    }

    @Transactional
    // OAuth 회원가입
    public Long oauthJoin(String oauthProvider, OAuthSignupRequestDto signupRequest) {
        // 닉네임 검증
        existsByNickname(signupRequest.getNickname());

        // String 값으로 넘어온 OAuth Provider 파싱
        OAuthProvider oAuthProvider = OAuthProvider.parsing(oauthProvider);
        // OAuthProvider enum 에 없는 값이라면
        if (oAuthProvider == null) {
            throw OAuthCustomException.UNSUPPORTED_OAUTH_PROVIDER;
        }

        // 회원 id (OAuthProvider 이름 + OAuthProviderId)
        String id = oAuthProvider + "_" + signupRequest.getOauthProviderId();

        User user =
                User.builder()
                        .id(id)
                        .nickname(signupRequest.getNickname())
                        .role(Role.USER)
                        .status(UserStatus.ALIVE)
                        .provider(oAuthProvider)
                        .providerId(signupRequest.getOauthProviderId())
                        .build();

        UserImage image =
                UserImage.builder().user(user).imagePath(imagePath + BASIC_IMAGE_NAME).build();

        user.updateImage(image);

        return userRepository.save(user).getUserId();
    }

    private void existsByNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw UserCustomException.ALREADY_USED_USER_NICKNAME;
        }
    }
}
