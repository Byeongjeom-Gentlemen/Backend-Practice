package com.sh.domain.comment.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.domain.comment.domain.Comment;
import com.sh.domain.comment.dto.CommentListResponseDto;
import com.sh.domain.comment.dto.SimpleCommentResponseDto;
import com.sh.domain.comment.repository.CommentRepository;
import com.sh.domain.user.domain.User;
import com.sh.domain.user.repository.UserRepository;
import com.sh.global.exception.customexcpetion.board.NotFoundBoardException;
import com.sh.global.exception.customexcpetion.comment.NotAuthorityException;
import com.sh.global.exception.customexcpetion.comment.NotFoundCommentException;
import com.sh.global.exception.customexcpetion.user.UserNotFoundException;
import com.sh.global.exception.errorcode.BoardErrorCode;
import com.sh.global.exception.errorcode.CommentErrorCode;
import com.sh.global.exception.errorcode.UserErrorCode;
import com.sh.global.util.SessionUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final SessionUtil sessionUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Override
    public Long createComment(Long boardId, String content) {
        Long userId = sessionUtil.getAttribute();

        User user =
                userRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(
                                () -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        Comment comment = Comment.builder().content(content).board(board).user(user).build();

        return commentRepository.save(comment).getCommentId();
    }

    // 댓글 조회(댓글 더보기)
    @Override
    public CommentListResponseDto selectCommentList(Pageable pageable, Long boardId) {

        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(
                                () -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        Slice<Comment> pageComment = commentRepository.findByBoardId(boardId, pageable);

        boolean hasNext = true;
        // 현재 페이지, 다음 페이지에 데이터가 없으면 프론트 영역에서 더보기 버튼이 사라진다고 가정
        if(pageComment.isLast() || !pageComment.hasNext() || !pageComment.hasContent()) {
            hasNext = false;
        }

        List<SimpleCommentResponseDto> commentList =
                pageComment.getContent().stream()
                        .filter(comment -> comment.getDelete_at() == null)
                        .map(comment -> SimpleCommentResponseDto.of(comment, comment.getUser()))
                        .collect(Collectors.toList());

        return CommentListResponseDto.of(hasNext, commentList);
    }

    // 댓글 수정
    @Override
    public void updateComment(Long boardId, Long commentId, String updateRequest) {
        Comment comment = verification(boardId, commentId);

        comment.update(updateRequest);
    }

    // 댓글 삭제
    @Override
    public void deleteComment(Long boardId, Long commentId) {
        Comment comment = verification(boardId, commentId);

        commentRepository.delete(comment);
    }

    // 검증 로직(댓글 수정, 댓글 삭제)
    private Comment verification(Long boardId, Long commentId) {
        Long userId = sessionUtil.getAttribute();

        userRepository
                .findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(UserErrorCode.NOT_FOUND_USER));

        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(
                                () -> new NotFoundBoardException(BoardErrorCode.NOT_FOUND_BOARD));

        if (board.getDelete_at() != null) {
            throw new NotFoundBoardException(BoardErrorCode.DELETED_BOARD);
        }

        Comment comment =
                commentRepository
                        .findByCommentId(commentId)
                        .orElseThrow(
                                () ->
                                        new NotFoundCommentException(
                                                CommentErrorCode.NOT_FOUND_COMMENT));

        if (comment.getDelete_at() != null) {
            throw new NotFoundCommentException(CommentErrorCode.DELETED_COMMENT);
        }

        if (userId != comment.getUser().getUserId()) {
            throw new NotAuthorityException(CommentErrorCode.NOT_AUTHORITY_COMMENT);
        }

        return comment;
    }
}
