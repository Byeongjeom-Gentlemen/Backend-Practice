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

    private final UserService userService;
    private final BoardService boardService;
    private final CommentRepository commentRepository;

    // 댓글 등록
    @Override
    public Long createComment(Long boardId, String content) {
        User user = userService.getLoginUser();
        Board board = boardService.queryBoard(boardId);
        board.verification();

        Comment comment = Comment.builder().content(content).board(board).user(user).build();

        return commentRepository.save(comment).getCommentId();
    }

    // 댓글 조회(댓글 더보기)
    @Override
    public CommentListResponseDto selectCommentList(Pageable pageable, Long boardId) {
        Board board = boardService.queryBoard(boardId);
        board.verification();

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
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new NotFoundCommentException(CommentErrorCode.NOT_FOUND_COMMENT));
    }

    // 작성자 검사
    private void checkWriter(Long writerId) {
        Long userId = userService.getLoginUser().getUserId();

        if(userId != writerId) {
            throw new NotAuthorityException(CommentErrorCode.NOT_AUTHORITY_COMMENT);
        }
    }
}
