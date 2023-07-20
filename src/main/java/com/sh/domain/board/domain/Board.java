package com.sh.domain.board.domain;

import com.sh.domain.board.dto.UpdateBoardRequestDto;
import com.sh.domain.user.domain.User;
import com.sh.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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

    @Column
    private String content;

    // 단방향 매핑
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private LocalDateTime delete_at;

    @Builder
    private Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.delete_at = null;
    }

    // 게시글 수정
    public void update(UpdateBoardRequestDto updateRequest) {
        this.title = updateRequest.getTitle();
        this.content = updateRequest.getContent();
    }
}
