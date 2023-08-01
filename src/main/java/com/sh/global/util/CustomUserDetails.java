package com.sh.global.util;

import com.sh.domain.user.domain.User;
import com.sh.domain.user.util.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String id;
    private String pw;
    private String role;

    public CustomUserDetails(User user) {
        this.userId = user.getUserId();
        this.id = user.getId();
        this.pw = user.getPw();
        this.role = user.getRole().name();
    }

    public CustomUserDetails(String id, String role) {
        this.id = id;
        this.role = role;
    }

    public CustomUserDetails(String id, String pw, String role) {
        this.id = id;
        this.pw = pw;
        this.role = role;
    }

    public static CustomUserDetails from(User user) {
        return new CustomUserDetails(user);
    }

    public static CustomUserDetails of(String id, String role) {
        return new CustomUserDetails(id, role);
    }

    public static CustomUserDetails of(String id, String pw, String role) {
        return new CustomUserDetails(id, pw, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return CustomAuthorityUtils.createAuthority(role);
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
