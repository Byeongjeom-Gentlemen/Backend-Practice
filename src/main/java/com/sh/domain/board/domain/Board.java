package com.sh.domain.board.domain;

import com.sh.domain.board.dto.UpdateBoardRequestDto;
import com.sh.domain.user.domain.User;
import com.sh.global.common.BaseTimeEntity;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE board SET delete_at = CURRENT_TIMESTAMP where board_id = ?")
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column private String content;

    // 단방향 매핑
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column private LocalDateTime delete_at;

    @Builder
    private Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // likeCount Default 값 설정
    @PrePersist
    public void prePersist() {
        this.likeCount = this.likeCount == null ? 0 : this.likeCount;
    }

    // 게시글 수정
    public void update(UpdateBoardRequestDto updateRequest) {
        this.title = updateRequest.getTitle();
        this.content = updateRequest.getContent();
    }

    // 좋아요 카운트 plus
    public void plusLike() {
        this.likeCount += 1;
    }

    // 좋아요 카운트 minus
    public void minusLike() {
        this.likeCount -= 1;
    }
}
