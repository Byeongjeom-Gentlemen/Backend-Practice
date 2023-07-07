package com.sh.domain.user.service;

import com.sh.domain.user.domain.Authority;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.LoginDto;
import com.sh.domain.user.dto.TokenDto;
import com.sh.domain.user.dto.UserRequestDto;
import com.sh.domain.user.dto.UserResponseDto;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.common.ApiResponse;
import com.sh.global.common.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
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

    // 회원 생성
    /*@Override
    public ResponseEntity<?> join(UserRequestDto userRequestDto) {
        // 아이디 중복확인
        if(checkById(userRequestDto.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>().fail(userRequestDto.getId(), "이미 사용중인 아이디입니다.", "409", "CONFLICT"));
        }

        // 닉네임 중복확인
        if(checkByNickname(userRequestDto.getNickname())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>().fail(userRequestDto.getNickname(), "이미 사용중인 닉네임입니다.", "409", "CONFLICT"));
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
        return ResponseEntity.ok()
                .body(new ApiResponse<>().success(user.getUserId(), "회원가입에 성공하였습니다."));
    }*/

    @Override
    public Long join(UserRequestDto userRequestDto) throws Exception {
        // 아이디 중복확인
        if(checkById(userRequestDto.getId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        // 닉네임 중복확인
        if(checkByNickname(userRequestDto.getNickname())) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
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
    public UserResponseDto login(LoginDto loginDto) throws Exception {
        /*User user = userRepository.findByUserId(loginDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("아이디를 확인해주세요."));

        if(!passwordEncoder.matches(loginDto.getPw(), user.getPw())) {
            throw new BadCredentialsException("비밀번호를 확인해주세요.");
        }*/

        /*return UserResponseDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .roles(user.getRoles())
                .result("success")
                .token(jwtProvider.createToken(user.getUserId(), user.getRoles()))
                .build();*/
        
        // 1. id / pw를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getId(), passwordEncoder.encode(loginDto.getPw()));

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailsService의 loadUserByUsername 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 생성
        TokenDto token = jwtProvider.generateToken(authentication);

        return UserResponseDto.builder()
                .id()
    }

    // 내 정보 조회
    @Override
    public UserResponseDto selectMe(HttpServletRequest request) {
        request.getHeader("")
    }
}
