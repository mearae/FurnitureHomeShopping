package com.example.funitureOnlineShop.product;

import com.example.funitureOnlineShop.core.utils.ApiUtils;
import com.example.funitureOnlineShop.productFile.ProductFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    // 상품 생성
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<ApiUtils.ApiResult<Long>> save(ProductResponse.SaveByIdDTO productResponseFind,
                                                         @RequestParam MultipartFile[] files) throws IOException {
        // 상품 저장 후 생성된 Product 객체를 반환받습니다.
        Product product = productService.save(productResponseFind, files);

        // Product 객체로부터 ID를 추출합니다.
        Long productId = product.getId();

        // ApiUtils의 success 메서드를 사용하여 성공 응답을 생성합니다.
        ApiUtils.ApiResult<Long> apiResult = ApiUtils.success(productId);

        // 생성된 응답을 클라이언트로 반환합니다.
        return ResponseEntity.ok(apiResult);
    }

/*
    // 전체 상품 조회
    @GetMapping("/products")
    public ResponseEntity<?> findAll(@RequestParam(required = false, defaultValue = "0") int page,
                                     @RequestParam(required = false, defaultValue = "10") int size) {
        Page<ProductResponse.findByCategoryForAllDTOS> products = productService.findAll(PageRequest.of(page, size));
        ApiUtils.ApiResult<Page<ProductResponse.findByCategoryForAllDTOS>> apiResult = ApiUtils.success(products);
        return ResponseEntity.ok(apiResult);
    }

 */

    // 상품과 해당 상품의 리뷰 찾기
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ProductResponse.FindByIdDTO productDTOS = productService.findById(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    // 해당 카테고리의 상품들 찾기
    @GetMapping("/category/{id}")
    public ResponseEntity<?> findByCategoryId(@PathVariable Long id) {
        List<ProductResponse.FindByCategoryDTO> productDTOS = productService.findByCategoryId(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(productDTOS);
        return ResponseEntity.ok(apiResult);
    }

    // 이미지들 찾기
    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImage(@PathVariable Long id) throws IOException {
        ProductFileResponse fileDto = productService.findByIdFile(id);

        File file = new File(fileDto.getFilePath() + fileDto.getUuid() + fileDto.getFileName());

        return ResponseEntity.ok()
                .header("Content-type", Files.probeContentType(file.toPath()))
                .body(FileCopyUtils.copyToByteArray(file));
    }

    // 상품 수정
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<?> update(ProductResponse.UpdateDTO updateDTO,
                                    @RequestParam MultipartFile[] files) throws IOException {
        ProductResponse.FindByCategoryDTO updatedProduct = productService.update(updateDTO, files);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(updatedProduct);
        return ResponseEntity.ok(apiResult);
    }

    // 상품 삭제
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        productService.delete(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(id);
        return ResponseEntity.ok(apiResult);
    }
}