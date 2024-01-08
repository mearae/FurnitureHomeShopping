package com.example.funitureOnlineShop.category;

import com.example.funitureOnlineShop.core.utils.ApiUtils;
import com.example.funitureOnlineShop.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 저장
    @PostMapping("/save")
    public ResponseEntity<?> save(@ModelAttribute CategoryRequest.SaveDto saveDto) {
        categoryService.save(saveDto);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // 등록된 최상위 카테고리 모두 조회
    @GetMapping("/super")
    public ResponseEntity<?> findAll() {
        List<CategoryResponse.FindAllDto> categories = categoryService.findAllSuper();

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(categories);
        return ResponseEntity.ok(apiResult);
    }

    // 부른 카테고리의 하위 카테고리 모두 조회
    @GetMapping("/son/{id}")
    public ResponseEntity<?> findAllSons(@PathVariable Long id) {
        List<CategoryResponse.FindAllDto> categories = categoryService.findAllSon(id);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(categories);
        return ResponseEntity.ok(apiResult);
    }

    // 요청 보낸 카테고리의 상위 카테고리와 하위 카테고리들 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        CategoryResponse.FindByIdDto categoryDto = categoryService.findById(id);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(categoryDto);
        return ResponseEntity.ok(apiResult);
    }

    // 카테고리의 이름과 그 상위 카테고리 수정
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public ResponseEntity<?> update(@ModelAttribute CategoryRequest.UpdateDto updateDto ) {
        categoryService.update(updateDto);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // 카테고리 삭제
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        categoryService.delete(id);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

}
