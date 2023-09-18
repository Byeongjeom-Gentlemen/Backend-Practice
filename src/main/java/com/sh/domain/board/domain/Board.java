package com.sh.domain.board.domain;

import com.sh.domain.user.domain.User;
import com.sh.global.common.BaseTimeEntity;
import com.sh.global.exception.customexcpetion.BoardCustomException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "like_count")
    private Integer likeCount;

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<BoardAttachedFile> attachedFiles = new ArrayList<>();

    @Column private LocalDateTime delete_at;

    @Builder
    private Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // likeCount, viewCount Default 값 설정
    @PrePersist
    public void prePersist() {
        this.likeCount = this.likeCount == null ? 0 : this.likeCount;
        this.viewCount = this.viewCount == null ? 0 : this.viewCount;
    }

    // 게시글 검증
    public void verification() {
        if (this.getDelete_at() != null) {
            throw BoardCustomException.DELETED_BOARD;
        }
    }

    // 게시글 수정
    public void update(String title, String comment) {
        this.title = title;
        this.content = comment;
    }

    // 좋아요 카운트 plus
    public void plusLike() {
        this.likeCount += 1;
    }

    // 좋아요 카운트 minus
    public void minusLike() {
        this.likeCount -= 1;
    }

    // 게시글 조회수 up
    public void addViewCount() {
        this.viewCount += 1;
    }

    // 게시글 첨부파일 등록
    public void setAttachedFiles(List<BoardAttachedFile> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    // 게시글 첨부파일 삭제
    // 연관관계를 끊고 고아 객체가 된 해당 게시글 첨부파일 엔티티가 자동으로 삭제됨
    public void removeAttachedFile(BoardAttachedFile attachedFile) {
        this.attachedFiles.remove(attachedFile);
    }

    // 게시글 첨부파일 추가
    // 해당 게시글 첨부파일 엔티티가 자동으로 생성됨
    public void addAttachedFile(BoardAttachedFile attachedFile) {
        this.attachedFiles.add(attachedFile);
    }
}
