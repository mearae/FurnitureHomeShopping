package com.example.funitureOnlineShop.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;


public class CategoryResponse {

    @Setter
    @Getter
    public static class FindAllDto{
        // 카테고리 id
        private Long id;
        // 카테고리 명
        private String categoryName;
        // 상위 카테고리 id
        private Long superCategory_id;

        public FindAllDto(Category category) {
            this.id = category.getId();
            this.categoryName = category.getCategoryName();
            if (category.getSuperCategory() != null)
                this.superCategory_id = category.getSuperCategory().getId();
            else
                this.superCategory_id = null;
        }
    }

    @Setter
    @Getter
    public static class FindByIdDto {
        // 카테고리 id
        private Long id;
        // 카테고리 명
        private String categoryName;
        // 상위 카테고리 id
        private CategoryDto superCategory;
        // 하위 카테고리 집합
        private List<CategoryDto> subCategories;

        public FindByIdDto(Category category, List<Category> subCategories) {
            this.id = category.getId();
            this.categoryName = category.getCategoryName();
            this.superCategory = new CategoryDto(category.getSuperCategory());
            this.subCategories = subCategories.stream()
                    .map(CategoryDto::new).collect(Collectors.toList());
        }

        @Setter
        @Getter
        // 하위 카테고리 Dto
        public class CategoryDto {
            // 하위 카테고리 id
            private Long id;
            // 하위 카테고리 명
            private String categoryName;

            public CategoryDto(Category category) {
                this.id = category.getId();
                this.categoryName = category.getCategoryName();
            }
        }
    }
}
