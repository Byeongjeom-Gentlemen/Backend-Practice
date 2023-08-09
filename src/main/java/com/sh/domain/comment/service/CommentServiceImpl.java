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
import com.sh.global.exception.customexcpetion.board.NotFoundBoardException;
import com.sh.global.exception.customexcpetion.comment.NotAuthorityException;
import com.sh.global.exception.customexcpetion.comment.NotFoundCommentException;
import com.sh.global.exception.errorcode.BoardErrorCode;
import com.sh.global.exception.errorcode.CommentErrorCode;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserService userService;

    // 댓글 등록
    @Override
    public Long createComment(Long boardId, String content) {
        User user = userService.getLoginUser();
        Board board = boardService.verificationBoard(boardId);

        Comment comment = Comment.builder()
                .content(content)
                .board(board)
                .user(user)
                .build();

        return commentRepository.save(comment).getCommentId();
    }

    // 댓글 조회(댓글 더보기)
    @Override
    public CommentListResponseDto selectCommentList(Pageable pageable, Long boardId) {
        Board board = boardService.verificationBoard(boardId);

        Slice<Comment> pageComment = commentRepository.findByBoardId(board.getId(), pageable);

        boolean hasNext = true;
        // 현재 페이지, 다음 페이지에 데이터가 없으면 프론트 영역에서 더보기 버튼이 사라진다고 가정
        if (pageComment.isLast() || !pageComment.hasNext() || !pageComment.hasContent()) {
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
        Comment comment = verificationComment(boardId, commentId);

        comment.update(updateRequest);
    }

    // 댓글 삭제
    @Override
    public void deleteComment(Long boardId, Long commentId) {
        Comment comment = verificationComment(boardId, commentId);

        commentRepository.delete(comment);
    }

    // 검증 로직(댓글 수정, 댓글 삭제)
    private Comment verificationComment(Long boardId, Long commentId) {
        User user = userService.getLoginUser();
        boardService.verificationBoard(boardId);

        Comment comment =
                commentRepository
                        .findByCommentId(commentId)
                        .orElseThrow(
                                () ->
                                        new NotFoundCommentException(
                                                CommentErrorCode.NOT_FOUND_COMMENT));

        // 댓글이 이미 삭제된 경우
        if (comment.getDelete_at() != null) {
            throw new NotFoundCommentException(CommentErrorCode.DELETED_COMMENT);
        }

        // 로그인한 회원과 댓글 작성자가 다른 경우
        if (user.getUserId() != comment.getUser().getUserId()) {
            throw new NotAuthorityException(CommentErrorCode.NOT_AUTHORITY_COMMENT);
        }

        return comment;
    }
}
