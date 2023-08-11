package com.sh.domain.user.domain;

import com.sh.domain.user.util.Role;
import com.sh.domain.user.util.UserStatus;
import com.sh.global.common.BaseTimeEntity;
import com.sh.global.exception.customexcpetion.UserCustomException;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 논리 삭제(실제 DB에서 삭제하지 않고 필드 값을 추가하여 삭제 여부를 판단
// @Where을 사용해 해당 값만 select하도록 설정(삭제된 회원은 조회 시 조회안됨)
// @Where(clause = "not user_status = 'WITHDRAWN'")
@SQLDelete(sql = "UPDATE users SET user_status = 'WITHDRAWN' where user_id = ?")
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 10, unique = true)
    private String id;

    @Column(nullable = false, length = 200)
    private String pw;

    @Column(nullable = false, length = 4, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 회원 상태
    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Builder
    private User(String id, String pw, String nickname, Role role, UserStatus status) {
        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
        this.role = role;
        this.status = status;
    }

    // 회원 검증 (탈퇴한 회원인지 검증)
    public void verification() {
        if (this.status == UserStatus.WITHDRAWN) {
            throw UserCustomException.WITHDRAWN_USER;
        }
    }

    // 회원 아이디 수정
    public void updateUserId(String id) {
        this.id = id;
    }

    // 회원 닉네임 수정
    public void updateUserNickname(String nickname) {
        this.nickname = nickname;
    }

    // 회원 비밀번호 수정
    public void updateUserPassword(String password) {
        this.pw = password;
    }
}
