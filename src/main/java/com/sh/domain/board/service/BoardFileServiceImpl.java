package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.BoardAttachedFile;
import com.sh.domain.board.dto.response.BoardFileResponseDto;
import com.sh.domain.board.repository.BoardAttachedFileRepository;
import com.sh.domain.board.repository.BoardRepository;
import com.sh.global.exception.customexcpetion.BoardCustomException;
import com.sh.global.util.file.FileResponseDto;
import com.sh.global.util.file.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardFileServiceImpl implements BoardFileService {

    @Value("${custom.boardFile-upload-path}")
    private String uploadPath;

    private final FileUtils fileUtils;
    private final BoardRepository boardRepository;
    private final BoardAttachedFileRepository attachedFileRepository;

    @Override
    public List<BoardAttachedFile> uploadAttachedFiles(Board board, List<MultipartFile> files) {
        // 실제 경로에 파일 업로드
        List<FileResponseDto> fileList = fileUtils.uploadFiles(uploadPath, files);

        // 업로드한 파일 정보를 BoardAttachedFile 형으로 변환
        List<BoardAttachedFile> attachedFiles =
                fileList.stream()
                        .map(
                                attachedFile ->
                                        BoardAttachedFile.builder()
                                                .board(board)
                                                .storeFileName(attachedFile.getStoreFileName())
                                                .originalFileName(
                                                        attachedFile.getOriginalFileName())
                                                .fileType(attachedFile.getFileType())
                                                .filePath(attachedFile.getFilePath())
                                                .fileSize(attachedFile.getFileSize())
                                                .build())
                        .collect(Collectors.toList());

        return attachedFiles;
    }

    // 게시글 첨부파일 조회
    @Override
    public List<BoardFileResponseDto> getBoardFiles(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> BoardCustomException.BOARD_NOT_FOUND);
        board.verification();

        List<BoardAttachedFile> fileList = attachedFileRepository.findAllByBoard(board);
        List<BoardFileResponseDto> result = new ArrayList<>();
        if(!fileList.isEmpty()) {
            result = fileList.stream()
                    .map(file -> BoardFileResponseDto.of(file.getOriginalFileName(), file.getFilePath(), file.getFileType()))
                    .collect(Collectors.toList());
        }

        return result;
    }

    // 게시글 첨부파일목록 삭제
    @Override
    public void deleteAttachedFiles(Board board) {
        List<BoardAttachedFile> fileList = board.getAttachedFiles();

        for(BoardAttachedFile file : fileList) {
            // 실제 경로에서 파일 삭제
            fileUtils.deleteFile(file.getFilePath());
        }
    }
}
