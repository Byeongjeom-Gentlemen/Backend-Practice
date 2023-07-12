package com.sh.domain.user.service;

import com.sh.domain.user.domain.Authority;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.LoginDto;
import com.sh.domain.user.dto.TokenDto;
import com.sh.domain.user.dto.UserRequestDto;
import com.sh.domain.user.dto.UserResponseDto;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.common.jwt.JwtProvider;
import com.sh.global.exception.customexcpetion.*;
import com.sh.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    public Long join(UserRequestDto userRequestDto) {
        // 아이디 중복확인
        if(checkById(userRequestDto.getId())) {
            throw new AlreadyUsedUserIdException("이미 사용중인 아이디입니다.");
        }

        // 닉네임 중복확인
        if(checkByNickname(userRequestDto.getNickname())) {
            throw new AlreadyUsedUserNicknameException("이미 사용중인 닉네임입니다.");
        }
        // 비밀번호 암호화
        userRequestDto.encryptPassword(passwordEncoder.encode(userRequestDto.getPw()));
        User user = User.builder()
                .userId(userRequestDto.getId())
                .pw(userRequestDto.getPw())
                .nickname(userRequestDto.getNickname())
                .build();
        // 권한 부여
        user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        userRepository.save(user);

        // 성공 시 pk값 반환
        return user.getId();
    }

    // 로그인
    @Override
    public UserResponseDto login(LoginDto loginDto) {
        // 1. id / pw를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPw());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailsService의 loadUserByUsername 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 생성
        TokenDto token = jwtProvider.generateToken(authentication);

        Optional<User> user = userRepository.findByUserId(loginDto.getId());
        return UserResponseDto.builder()
                .id(user.get().getId())
                .userId(user.get().getUserId())
                .nickname(user.get().getNickname())
                .createdDate(user.get().getCreatedDate())
                .modifiedDate(user.get().getModifiedDate())
                .roles(user.get().getRoles())
                .token(token)
                .build();
    }

    // 내 정보 조회
    @Override
    public UserResponseDto selectMe() {
        String userId = SecurityUtil.getCurrentUserId();
        Optional<User> user = userRepository.findByUserId(userId);

        return UserResponseDto.builder()
                .id(user.get().getId())
                .userId(user.get().getUserId())
                .nickname(user.get().getNickname())
                .createdDate(user.get().getCreatedDate())
                .modifiedDate(user.get().getModifiedDate())
                .build();
    }

    // 회원 삭제
    // 나중에 회원이 삭제되면 회원이 등록한 게시글, 댓글등도 같이 삭제
    // CASCADE 옵션은 되도록 사용하지 않는다.
    @Override
    public void deleteUser(LoginDto users) {
        // case01
        /*// 회원 아이디가 없는 경우(null일 경우)
        User me = userRepository.findByUserId(users.getId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 이미 탈퇴한 회원인 경우
        if(me.getWithdrawalDate() != null) {
            throw new UserNotFoundException("이미 탈퇴한 회원입니다.");
        }
        
        // 입력한 아이디와 비밀번호가 맞지 않는 경우
        if(!passwordEncoder.matches(me.getPw(), users.getPw())) {
            throw new NotMatchesUserException("사용자 인증에 실패하였습니다.");
        }*/


        // case02
        userRepository.findByUserId(users.getId())
                .map(data -> {
                    if(data.getWithdrawalDate() != null) {
                        throw new UserNotFoundException();
                    }
                    else if (!passwordEncoder.matches(data.getPw(), users.getPw())) {
                        throw new NotMatchesUserException("사용자 인증에 실패하였습니다.");
                    }
                    userRepository.delete(data);
                    return data;
                }).orElseThrow(() -> new UserNotFoundException());


        // case03
        /*Optional<User> user = userRepository.findByUserId(users.getId());
        if(!user.isPresent()) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        user.ifPresent(data -> {
            if(data.getWithdrawalDate() != null) {
                throw new UserNotFoundException("탈퇴한 회원입니다.");
            }
            else if (!passwordEncoder.matches(data.getPw(), users.getPw())) {
                throw new NotMatchesUserException("사용자 인증에 실패하였습니다.");
            }
        });*/

    }
}
