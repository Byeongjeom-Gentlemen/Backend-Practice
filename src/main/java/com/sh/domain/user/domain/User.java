package com.sh.domain.user.domain;

import com.sh.global.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 10, unique = true, name = "id")
    private String id;

    @Column(nullable = false, length = 200, name = "pw")
    private String pw;

    @Column(nullable = false, length = 4, name = "nickname")
    private String nickname;

    @Builder
    public User(String id, String pw, String nickname) {
        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
    }
}
