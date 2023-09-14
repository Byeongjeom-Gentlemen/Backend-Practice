package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.BoardAttachedFile;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface BoardFileService {

    // 게시글 첨부파일 업로드
    List<BoardAttachedFile> uploadAttachedFiles(Board board, List<MultipartFile> files);
}
