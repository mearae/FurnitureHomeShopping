package com.example.funitureOnlineShop.commentFile;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentFileDto {
    private Long id;
    // 파일 경로
    private String filePath;
    // 파일 명
    private String fileName;
    // 파일 형식
    private String fileType;
    // 랜덤 키
    private String uuid;
    // 파일 크기
    private Long fileSize;

    public CommentFile toEntity(){
        return CommentFile.builder()
                .id(id)
                .filePath(filePath)
                .fileName(fileName)
                .uuid(uuid)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }

    public static CommentFileDto toFileDto(CommentFile commentFile){
        return new CommentFileDto(
                commentFile.getId(),
                commentFile.getFilePath(),
                commentFile.getFileName(),
                commentFile.getFileType(),
                commentFile.getUuid(),
                commentFile.getFileSize());
    }
}
