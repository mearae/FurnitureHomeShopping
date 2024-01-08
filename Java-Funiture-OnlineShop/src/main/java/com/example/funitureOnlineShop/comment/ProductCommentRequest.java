package com.example.funitureOnlineShop.comment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class ProductCommentRequest {

    @Setter
    @Getter
    public static class SaveDto {
        // 별점( 1 ~ 5점 )
        @NotEmpty
        private int star;
        // 내용
        @NotEmpty
        private String contents;
        // 작성일
        private LocalDateTime createTime;
        // 수정일
        private LocalDateTime updateTime;
        // 주문 내역 id
        @NotEmpty
        private Long orderCheckId;

        public ProductComment toEntity() {
            return ProductComment.builder()
                    .star(star)
                    .contents(contents)
                    .createTime(createTime)
                    .updateTime(LocalDateTime.now())
                    .build();
        }
    }

    @Setter
    @Getter
    public static class UpdateDto {
        // 상품 후기 id
        @NotEmpty
        private Long id;
        // 별점( 1 ~ 5점 )
        @NotEmpty
        private int star;
        // 내용
        @NotEmpty
        private String contents;
    }
}
