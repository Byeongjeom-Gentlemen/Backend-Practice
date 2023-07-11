package com.sh.global.security;

import com.sh.domain.user.domain.Authority;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.EmptyStackException;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws BadCredentialsException {
        return userRepository.findByUserId(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new BadCredentialsException("사용자 인증에 실패하였습니다. 아이디 혹은 비밀번호를 확인하세요."));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(User user) {
        CustomUserDetails users = new CustomUserDetails(user);

        return org.springframework.security.core.userdetails.User.builder()
                .username(users.getUsername())
                .password(users.getPassword())
                .roles(String.valueOf(users.getAuthorities().stream()))
                .build();
    }
}
