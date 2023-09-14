package com.sh.domain.board.service;

import com.sh.domain.board.domain.Board;
import com.sh.domain.board.domain.BoardAttachedFile;
import com.sh.global.util.file.FileResponseDto;
import com.sh.global.util.file.FileUtils;
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
}
