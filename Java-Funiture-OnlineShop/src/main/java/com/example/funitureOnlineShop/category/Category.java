package com.example.funitureOnlineShop.category;

import com.example.funitureOnlineShop.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 카테고리 ID
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category superCategory;

    @OneToMany(mappedBy = "superCategory", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Category> subCategories;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products =  new ArrayList<>();

    @Builder
    public Category(Long id, String categoryName, Category superCategory, List<Category> subCategories, List<Product> products) {
        this.id = id;
        this.categoryName = categoryName;
        this.superCategory = superCategory;
        this.subCategories = subCategories;
        this.products = products;
    }

    public void updateSuperCategory(Category superCategory) {
        this.superCategory = superCategory;
    }

    public void updateFromDto(CategoryRequest.UpdateDto updateDto) {
        this.categoryName = updateDto.getCategoryName();
    }
}
