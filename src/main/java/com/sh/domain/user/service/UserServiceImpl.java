package com.sh.domain.user.service;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.dto.LoginDto;
import com.sh.domain.user.dto.UserDto;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.common.ApiResponse;
import com.sh.global.common.jwt.JwtToken;
import com.sh.global.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 아이디 중복 확인
    @Override
    public boolean checkById(String id) {
        return userRepository.existsById(id);
    }

    // 닉네임 중복 확인
    @Override
    public boolean checkByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 회원 생성
    @Override
    public ResponseEntity<?> join(UserDto userDto) {
        // 아이디 중복확인
        if(checkById(userDto.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>().fail(userDto.getId(), "이미 사용중인 아이디입니다.", "409", "CONFLICT"));
        }

        // 비밀번호 중복확인
        if(checkByNickname(userDto.getNickname())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>().fail(userDto.getNickname(), "이미 사용중인 닉네임입니다.", "409", "CONFLICT"));
        }
        // 비밀번호 암호화
        userDto.encryptPassword(passwordEncoder.encode(userDto.getPw()));
        User user = userRepository.save(userDto.toEntity());

        // 성공 시 pk값 반환
        return ResponseEntity.ok()
                .body(new ApiResponse<>().success(user.getUserId(), "회원가입에 성공하였습니다."));
    }

    // 로그인
    @Override
    public ResponseEntity<?> login(LoginDto loginDto) {
        Optional<User> user = userRepository.findById(loginDto.getId());
        if(user.orElse(null) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>().errors("404", "해당 아이디를 가진 회원이 존재하지 않습니다."));
        }

        if(!passwordEncoder.matches(loginDto.getPw(), user.get().getPw())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>().fail(loginDto, "비밀번호가 일치하지 않습니다.", "400", "BAD_REQUEST"));
        }

        System.out.println(user.get().getId());
        System.out.println(user.get().getPw());
        // Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPw());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 검증된 인증 정보로 JWT 토큰 생성
        JwtToken token = jwtTokenProvider.generateToken(authentication);
        System.out.println(token.getGrantType());
        System.out.println(token.getAccessType());
        System.out.println(token.getRefreshType());
        
        // user 정보와 JWT 토큰정보를 함께 응답
        HashMap<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("result", token);

        return ResponseEntity.ok().body(new ApiResponse<>().success(response, "로그인이 성공하였습니다."));
    }
}
