package com.sh.domain.user.domain;

import com.sh.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String name;

    @Column
    private String originalName;

    @Column(nullable = false)
    private String filePath;

    @Builder
    public UserImage(User user, String name, String originalName, String filePath) {
        this.user = user;
        this.name = name;
        this.originalName = originalName;
        this.filePath = filePath;
    }

    public void updatePath(String path) {
        this.filePath = path;
    }
}
