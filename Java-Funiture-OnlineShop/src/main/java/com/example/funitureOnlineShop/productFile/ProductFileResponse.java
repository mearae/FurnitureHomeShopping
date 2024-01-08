package com.example.funitureOnlineShop.productFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Setter
@Getter
public class ProductFileResponse {

    // 이미지 id
    private Long id;

    // ** 파일 경로
    private String filePath;

    // ** 파일 이름
    private String fileName;

    // uuid (랜덤 키)
    private String uuid;

    // ** 파일 포멧
    private String fileType;

    // ** 파일 크기
    private Long fileSize;

    public ProductFileResponse(ProductFile productFile) {
        this.id = productFile.getId();
        this.filePath = productFile.getFilePath();
        this.fileName = productFile.getFileName();
        this.uuid = productFile.getUuid();
        this.fileType = productFile.getFileType();
        this.fileSize = productFile.getFileSize();
    }

    public ProductFile toEntity() {
        return ProductFile.builder()
                .filePath(filePath)
                .fileName(fileName)
                .uuid(uuid)
                .fileType(fileType)
                .fileSize(fileSize)
                .build();
    }

    public static ProductFileResponse toDto(ProductFile productFile) {
        return new ProductFileResponse(
                productFile.getId(),
                productFile.getFilePath(),
                productFile.getFileName(),
                productFile.getUuid(),
                productFile.getFileType(),
                productFile.getFileSize());
    }
}
