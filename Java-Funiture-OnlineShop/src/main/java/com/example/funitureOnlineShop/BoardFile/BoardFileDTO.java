package com.example.funitureOnlineShop.boardFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Setter
@Getter
public class BoardFileDTO {
    private Long id;

    private String filePath;

    private String fileName;

    private String uuid;

    private String fileType;

    private Long fileSize;

    public BoardFile toEntity(){
        return BoardFile.builder()
                .filePath(filePath)
                .fileName(fileName)
                .uuid(uuid)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }

}
