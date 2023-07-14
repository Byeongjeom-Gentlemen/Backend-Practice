package com.sh.domain.user.service;

import com.sh.domain.user.domain.Authority;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.*;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.common.jwt.JwtProvider;
import com.sh.global.exception.customexcpetion.*;
import com.sh.global.security.SecurityUtil;
import com.sh.global.common.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 아이디 중복 확인
    @Override
    public boolean checkById(String userId) {
        return userRepository.existsByUserId(userId);
    }

    // 닉네임 중복 확인
    @Override
    public boolean checkByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public Long join(SignupRequestDto signupRequestDto) {
        // 아이디 중복확인
        if(checkById(signupRequestDto.getId())) {
            throw new AlreadyUsedUserIdException("이미 사용중인 아이디입니다.");
        }

        // 닉네임 중복확인
        if(checkByNickname(signupRequestDto.getNickname())) {
            throw new AlreadyUsedUserNicknameException("이미 사용중인 닉네임입니다.");
        }
        // 비밀번호 암호화
        signupRequestDto.encryptPassword(passwordEncoder.encode(signupRequestDto.getPw()));
        User user = User.builder()
                .userId(signupRequestDto.getId())
                .pw(signupRequestDto.getPw())
                .nickname(signupRequestDto.getNickname())
                .build();
        // 권한 부여
        user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        userRepository.save(user);

        // 성공 시 pk값 반환
        return user.getId();
    }

    // 로그인
    @Override
    public UserResponseDto login(UserRequestDto userRequestDto) {
        // 1. id / pw를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userRequestDto.getId(), userRequestDto.getPw());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailsService의 loadUserByUsername 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 생성
        TokenDto token = jwtProvider.generateToken(authentication);

        User user = userRepository.findByUserId(userRequestDto.getId())
                .orElseThrow(() -> new UserNotFoundException());

        return UserResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .roles(user.getRoles())
                .token(token)
                .build();
    }

    // 내 정보 조회
    @Override
    public UserResponseDto selectMe() {
        String userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException());

        return UserResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .build();
    }

    // 회원 삭제
    // 나중에 회원이 삭제되면 회원이 등록한 게시글, 댓글등도 같이 삭제
    // CASCADE 옵션은 되도록 사용하지 않는다.
    @Override
    public void deleteUser(UserRequestDto users) {
        userRepository.delete(userRepository.findByUserId(users.getId())
                .map(data -> {
                    if(data.getStatus().equals(UserStatus.WITHDRAWAL_USER.getStatus())) {
                        throw new UserNotFoundException();
                    }
                    else if (!passwordEncoder.matches(users.getPw(), data.getPw())) {
                        throw new NotMatchesUserException();
                    }
                    return data;
                }).orElseThrow(() -> new UserNotFoundException()));
    }

    // 회원 수정(PATCH)
    @Override
    public void modifyMe(UpdateUserRequestDto user) {
        User afterUser = userRepository.findByUserId(user.getUserId())
                .map(data -> {
                    if(data.getStatus().equals(UserStatus.WITHDRAWAL_USER)) {
                        throw new UserNotFoundException();
                    }
                    return data;
                })
                .orElseThrow(() -> new UserNotFoundException());

        // 아이디 수정 시
        if(user.getAfterId() != null && userRepository.existsByUserId(user.getAfterId())) {
            throw new AlreadyUsedUserIdException("이미 사용중인 아이디입니다.");
        } else if (user.getAfterId() == null) {
            user.setAfterId(afterUser.getUserId());
        }

        // 닉네임 수정 시
        if(user.getAfterNickname() != null && userRepository.existsByNickname(user.getAfterNickname())) {
            throw new AlreadyUsedUserNicknameException("이미 사용중인 닉네임입니다.");
        } else if (user.getAfterNickname() == null) {
            user.setAfterNickname(afterUser.getNickname());
        }

        // 비밀번호 수정 시
        if(user.getAfterPw() != null) {
            user.encryptPassword(passwordEncoder.encode(user.getAfterPw()));
        } else {
            user.setAfterPw(afterUser.getPw());
        }

        afterUser.updateUser(user);
    }

}
