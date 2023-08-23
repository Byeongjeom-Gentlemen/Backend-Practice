package com.sh.domain.comment.repository;

import static com.sh.domain.comment.domain.QComment.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<SimpleCommentResponseDto> findByCommentIdLessThanOrderByCreatedAtDesc(
            Long lastCommentId, Long boardId, Pageable pageable) {

        List<SimpleCommentResponseDto> results =
                queryFactory
                        .select(
                                Projections.constructor(
                                        SimpleCommentResponseDto.class,
                                        comment.commentId,
                                        comment.content,
                                        comment.user,
                                        comment.createdDate,
                                        comment.modifiedDate))
                        .from(comment)
                        .where(
                                // no-offset 페이징 처리
                                ltCommentId(lastCommentId),
                                comment.board.id.eq(boardId),
                                comment.delete_at.isNull())
                        .orderBy(comment.createdDate.desc())
                        .limit(pageable.getPageSize() + 1)
                        .fetch();

        return checkLastPage(pageable, results);
    }

    private BooleanExpression ltCommentId(Long lastCommentId) {
        // lastCommentId == null 이면 처음 page
        if (lastCommentId == null) {
            return null;
        }

        return comment.commentId.lt(lastCommentId);
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<SimpleCommentResponseDto> checkLastPage(
            Pageable pageable, List<SimpleCommentResponseDto> result) {
        boolean hasNext = false;

        if (result.size() > pageable.getPageSize()) {
            hasNext = true;
            result.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
