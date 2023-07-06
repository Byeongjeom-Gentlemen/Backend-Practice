package com.sh.domain.user.domain;

import com.sh.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10, unique = true, name = "user_id")
    private String userId;

    @Column(nullable = false, length = 200)
    private String pw;

    @Column(nullable = false, length = 4, unique = true)
    private String nickname;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    /*@Builder
    public User(String userId, String pw, String nickname) {
        this.userId = userId;
        this.pw = pw;
        this.nickname = nickname;
    }*/

    // 권한 설정
    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setUser(this));
    }
}
