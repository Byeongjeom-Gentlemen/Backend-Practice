package com.sh.domain.user.domain;

import com.sh.global.common.BaseTimeEntity;
import com.sh.global.util.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 논리 삭제(실제 DB에서 삭제하지 않고 필드 값을 추가하여 삭제 여부를 판단
// @Where을 사용해 해당 값만 select하도록 설정(삭제된 회원은 조회 시 조회안됨)
@Where(clause = "not user_status = 'withdrawn'")
@SQLDelete(sql = "UPDATE users SET user_status = 'withdrawn' where id = ?")
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

    @OneToMany(mappedBy = "users", orphanRemoval = true)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    // 회원 상태
    @Column(name = "user_status")
    @Builder.Default
    private String status = UserStatus.ALIVE_USER.getStatus();

    // 권한 설정
    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setUser(this));
    }
}
