package com.example.funitureOnlineShop.comment;

import com.example.funitureOnlineShop.commentFile.CommentFile;
import com.example.funitureOnlineShop.orderCheck.OrderCheck;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table
public class ProductComment {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 내용
    @Column(length = 1000)
    private String contents;
    // 별점( 1 ~ 5점 )
    @Column(length = 1, nullable = false)
    private int star;
    // 생성일
    @Column(length = 30, nullable = false)
    private LocalDateTime createTime;
    // 수정일
    @Column(length = 30, nullable = false)
    private LocalDateTime updateTime;

    @OneToOne(fetch = FetchType.LAZY)
    private OrderCheck orderCheck;

    @OneToMany(mappedBy = "productComment", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentFile> commentFiles = new ArrayList<>();

    @Builder
    public ProductComment(Long id, String contents, int star, LocalDateTime createTime, LocalDateTime updateTime, OrderCheck orderCheck, List<CommentFile> commentFiles) {
        this.id = id;
        this.contents = contents;
        this.star = star;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.orderCheck = orderCheck;
        this.commentFiles = commentFiles;
    }

    public void updateFromEntity(OrderCheck orderCheck) {
        this.orderCheck = orderCheck;
        this.createTime = LocalDateTime.now();
    }

    public void updateFromDto(ProductCommentRequest.UpdateDto updateDto) {
        this.contents = updateDto.getContents();
        this.star = updateDto.getStar();
        this.updateTime = LocalDateTime.now();
        this.commentFiles.clear();
    }
}
