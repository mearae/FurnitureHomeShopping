package com.example.funitureOnlineShop.productFile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductFileRepository extends JpaRepository<ProductFile, Long> {
    List<ProductFile> findByProductId(Long productId);

    void deleteAllByProductId(Long id);
}
