package com.example.funitureOnlineShop.category;

import lombok.Getter;
import lombok.Setter;

public class CategoryRequest {

    @Setter
    @Getter
    public static class SaveDto {
        // 상위 카테고리 id
        private Long superCategory_id;
        // 카테고리 명
        private String categoryName;
        // 엔티티로 변경
        public Category toEntity(){
            return Category.builder()
                    .categoryName(categoryName)
                    .build();
        }
    }

    @Setter
    @Getter
    public static class UpdateDto {
        // 카테고리 id
        private Long id;
        // 카테고리 명
        private String categoryName;
        // 엔티티로 변경
        public Category toEntity(){
            return Category.builder()
                    .id(id)
                    .categoryName(categoryName)
                    .build();
        }
    }
}
