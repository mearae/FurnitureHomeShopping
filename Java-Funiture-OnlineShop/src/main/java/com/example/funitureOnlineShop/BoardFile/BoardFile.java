package com.example.funitureOnlineShop.boardFile;

import com.example.funitureOnlineShop.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** 파일 경로
    private String filePath;

    // ** 파일 이름
    private String fileName;

    // ** uuid (랜덤 키)
    private String uuid;

    // ** 파일 포멧
    private String fileType;

    // ** 파일 크기
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Builder
    public BoardFile(Long id, String filePath, String fileName , String uuid, String fileType, Long fileSize, Board board) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uuid = uuid;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.board = board;
    }

    public void updateFromDTO(BoardFile boardFile){

        this.filePath = boardFile.getFilePath();
        this.fileName = boardFile.getFileName();
        this.uuid = boardFile.getUuid();
        this.fileType = boardFile.getFileType();
        this.fileSize = boardFile.getFileSize();
        this.board = boardFile.getBoard();
    }
}
