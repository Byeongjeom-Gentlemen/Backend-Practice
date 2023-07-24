package com.sh.domain.comment.domain;

import com.sh.domain.board.domain.Board;
import com.sh.domain.user.domain.User;
import com.sh.global.common.BaseTimeEntity;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update comment set delete_at = CURRENT_TIMESTAMP where comment_id = ?")
public class Comment extends BaseTimeEntity {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column private LocalDateTime delete_at;

    @Builder
    private Comment(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
        this.delete_at = null;
    }

    // 댓글 수정
    public void update(String content) {
        this.content = content;
    }
}
