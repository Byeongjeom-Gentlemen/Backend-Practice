package com.sh.domain.user.domain;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column private String refreshTokenName;

    @Column private LocalDateTime createdDate;

    @Builder
    private RefreshToken(String userId, String refreshTokenName) {
        this.userId = userId;
        this.refreshTokenName = refreshTokenName;
        this.createdDate = LocalDateTime.now();
    }
}
