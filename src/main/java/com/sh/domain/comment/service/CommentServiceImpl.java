package com.sh.domain.comment.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.board.service.BoardService;
import com.sh.domain.comment.domain.Comment;
import com.sh.domain.comment.dto.CommentListResponseDto;
import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import com.sh.domain.comment.repository.CommentRepository;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.service.UserService;
import com.sh.global.exception.customexcpetion.BoardCustomException;
import com.sh.global.exception.customexcpetion.CommentCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 댓글 등록
    @Override
    public Long createComment(Long boardId, String content) {
        User user = userService.getLoginUser();
        Board board = queryBoard(boardId);
        board.verification();

        Comment comment = Comment.builder().content(content).board(board).user(user).build();

        return commentRepository.save(comment).getCommentId();
    }

    // 댓글 조회(댓글 더보기)
    @Override
    public CommentListResponseDto selectCommentList(Long boardId, Long lastCommentId) {
        Board board = queryBoard(boardId);
        board.verification();

        // page : 0, size : 5 고정, no-offset 방식
        PageRequest pageable = PageRequest.of(0, 5);

        // jpql
        /*
        Slice<Comment> pageCommentInJpql = commentRepository
        		.findByCommentIdLessThanOrderByCreatedAtDescInJpql(lastCommentId, boardId, pageable);

        List<SimpleCommentResponseDto> commentList = pageCommentInJpql.getContent()
        		.stream()
        		.map(comment -> SimpleCommentResponseDto.of(comment, comment.getUser()))
        		.collect(Collectors.toList());
         */

        // querydsl
        Slice<SimpleCommentResponseDto> pageComment =
                commentRepository.findByCommentIdLessThanOrderByCommentIdDesc(
                        lastCommentId, boardId, pageable);

        return CommentListResponseDto.of(pageComment.hasNext(), pageComment.getContent());
    }

    private Board queryBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> BoardCustomException.BOARD_NOT_FOUND);
    }

    // 댓글 수정
    @Override
    public void updateComment(Long boardId, Long commentId, String updateRequest) {
        Comment comment = queryComment(commentId);
        comment.verification();
        checkWriter(comment.getUser().getUserId());

        comment.update(updateRequest);
    }

    // 댓글 삭제
    @Override
    public void deleteComment(Long boardId, Long commentId) {
        Comment comment = queryComment(commentId);
        comment.verification();
        checkWriter(comment.getUser().getUserId());

        commentRepository.delete(comment);
    }

    // 댓글 조회
    private Comment queryComment(Long commentId) {
        return commentRepository
                .findByCommentId(commentId)
                .orElseThrow(() -> CommentCustomException.COMMENT_NOT_FOUND);
    }

    // 작성자 검사
    private void checkWriter(Long writerId) {
        Long userId = userService.getLoginUser().getUserId();

        if (userId != writerId) {
            throw CommentCustomException.NOT_AUTHORITY_COMMENT;
        }
    }
}
