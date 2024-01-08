package com.example.funitureOnlineShop.commentFile;

import com.example.funitureOnlineShop.comment.ProductComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class CommentFile {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 파일 경로
    @Column(nullable = false)
    private String filePath;
    // 파일 명
    @Column(nullable = false)
    private String fileName;
    // uuid
    @Column(nullable = false)
    private String uuid;
    // 파일 형식
    @Column(nullable = false)
    private String fileType;
    // 파일 크기
    @Column
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProductComment productComment;

    @Builder
    public CommentFile(Long id, String filePath, String fileName, String uuid, String fileType, Long fileSize, ProductComment productComment) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uuid = uuid;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.productComment = productComment;
    }

    public void updateFromComment(ProductComment comment){
        this.productComment = comment;
    }
}
