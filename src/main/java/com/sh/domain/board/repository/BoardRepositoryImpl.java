package com.sh.domain.board.repository;

import static com.sh.domain.board.domain.QBoard.board;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sh.domain.board.dto.response.SimpleBoardResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SimpleBoardResponseDto> findByBoardIdLessThanBoardInOrderByBoardIdDescAndSearch(
            Long lastBoardId, String searchType, String keyword, Pageable pageable) {
        List<SimpleBoardResponseDto> result =
                queryFactory
                        .select(
                                Projections.constructor(
                                        SimpleBoardResponseDto.class,
                                        board.id,
                                        board.title,
                                        board.user.nickname,
                                        board.createdDate))
                        .from(board)
                        .where(
                                // no-offset 페이징 처리
                                ltBoardId(lastBoardId),
                                // 검색 처리
                                eqOrLikeKeyword(searchType, keyword),
                                board.delete_at.isNull())
                        .orderBy(board.id.desc())
                        .limit(pageable.getPageSize())
                        .fetch();

        return result;
    }

    /* 동적 쿼리 */

    private BooleanExpression ltBoardId(Long lastBoardId) {
        if (lastBoardId == null) {
            // null 이면 해당 조건절 무시
            return null;
        }

        // board.id < lastBoardId
        return board.id.lt(lastBoardId);
    }

    // 동적 쿼리
    private BooleanExpression eqOrLikeKeyword(String searchType, String keyword) {
        // 전체 조회
        if (searchType.equals("all")) {
            return null;
        }

        // 제목으로 조회
        if (searchType.equals("title")) {
            return board.title.like(keyword + "%");
        }

        // 작성자로 조회
        return board.user.nickname.eq(keyword);
    }
}
