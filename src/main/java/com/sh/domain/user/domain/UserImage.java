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
    @Column(name = "file_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "save_name")
    private String storeName;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "image_path")
    private String imagePath;

    @Builder
    public UserImage(User user, String storeName, String originalName, String imagePath) {
        this.user = user;
        this.storeName = storeName;
        this.originalName = originalName;
        this.imagePath = imagePath;
    }

    public void updatePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
