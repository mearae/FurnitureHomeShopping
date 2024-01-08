package com.example.funitureOnlineShop.comment;

import com.example.funitureOnlineShop.commentFile.CommentFile;
import com.example.funitureOnlineShop.commentFile.CommentFileDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ProductCommentResponse {

    @Setter
    @Getter
    @AllArgsConstructor
    @ToString
    public static class CommentDto {
        // PK
        private Long id;
        // 별점( 1 ~ 5점 )
        private int star;
        // 작성자 명
        private String writer;
        // 내용
        private String contents;
        // 옵션 명
        private String optionName;
        // 작성일
        private LocalDateTime createTime;
        // 수정일
        private LocalDateTime updateTime;
        // 주문 내역 id
        private Long orderCheckId;
        // 옵션 id
        private Long optionId;
        // 상품 id
        private Long productId;
        // 작성자 id
        private Long userId;
        // 사진들
        private List<CommentFileDto> files;

        // dto로 변경
        public static CommentDto toDto(ProductComment comment, List<CommentFile> files) {
            return new CommentDto(
                    comment.getId(),
                    comment.getStar(),
                    comment.getOrderCheck().getUser().getUsername(),
                    comment.getContents(),
                    comment.getOrderCheck().getOption().getOptionName(),
                    comment.getCreateTime(),
                    comment.getUpdateTime(),
                    comment.getOrderCheck().getId(),
                    comment.getOrderCheck().getOption().getId(),
                    comment.getOrderCheck().getOption().getProduct().getId(),
                    comment.getOrderCheck().getUser().getId(),
                    files.stream().map(CommentFileDto::toFileDto).collect(Collectors.toList()));
        }

        // 작성일을 기준으로 리스트를 정렬 (최신순)
        public static void sortByCreateDate(List<CommentDto> comments) {
            comments.sort(Comparator.comparing(CommentDto::getCreateTime).reversed());
        }
    }
}
