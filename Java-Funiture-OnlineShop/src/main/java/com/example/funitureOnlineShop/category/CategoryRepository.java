package com.example.funitureOnlineShop.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findBySuperCategoryId(Long id);

    List<Category> findBySuperCategoryIsNull();
}
