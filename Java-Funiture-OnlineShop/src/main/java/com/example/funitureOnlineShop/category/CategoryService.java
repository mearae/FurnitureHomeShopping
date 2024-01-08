package com.example.funitureOnlineShop.category;

import com.example.funitureOnlineShop.core.error.exception.Exception404;
import com.example.funitureOnlineShop.core.error.exception.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 저장
    @Transactional
    public void save(CategoryRequest.SaveDto saveDto) {
        // 최상위 카테고리 추가 시
        if (saveDto.getSuperCategory_id() == 0L){
            try {
                categoryRepository.save(saveDto.toEntity());
            } catch (Exception e){
                throw new Exception500("카테고리 저장 도중 이상이 생겼습니다.");
            }
        // 하위 카테고리 추가 시
        } else {
            // 요청 보낸 상위 카테고리가 없을 경우
            Optional<Category> optionalCategory = categoryRepository.findById(saveDto.getSuperCategory_id());
            if (optionalCategory.isEmpty())
                throw new Exception404("해당하는 상위 카테고리를 찾을 수 없습니다. : " + saveDto.getSuperCategory_id());
            // 카테고리 저장
            try {
                Category category = categoryRepository.save(saveDto.toEntity());
                category.updateSuperCategory(optionalCategory.get());
            } catch (Exception e) {
                throw new Exception500("카테고리 저장 도중 이상이 생겼습니다.");
            }
        }
    }

    // 등록된 모든 최상위 카테고리 탐색
    public List<CategoryResponse.FindAllDto> findAllSuper() {
        List<Category> categories = categoryRepository.findBySuperCategoryIsNull();

        return categories.stream()
                .map(CategoryResponse.FindAllDto::new).collect(Collectors.toList());
    }

    // 하위 카테고리 탐색
    public List<CategoryResponse.FindAllDto> findAllSon(Long id) {
        List<Category> categories = categoryRepository.findBySuperCategoryId(id);

        return categories.stream()
                .map(CategoryResponse.FindAllDto::new).collect(Collectors.toList());
    }

    // 요청 받은 카테고리와 그 상하위 카테고리 탐색
    public CategoryResponse.FindByIdDto findById(Long id) {
        // 요청 받은 카테고리
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 카테고리를 찾을 수 없습니다. : " + id));
        // 하위 카테고리
        List<Category> subCategories = categoryRepository.findBySuperCategoryId(id);

        return new CategoryResponse.FindByIdDto(category, subCategories);
    }

    // 카테고리의 이름 수정
    @Transactional
    public void update(CategoryRequest.UpdateDto updateDto) {
        // 수정할 카테고리 존재?
        Optional<Category> optionalCategory = categoryRepository.findById(updateDto.getId());
        if (optionalCategory.isEmpty())
            throw new Exception404("해당 카테고리를 찾을 수 없습니다. : " + updateDto.getId());
        // 카테고리 수정
        Category category = optionalCategory.get();
        try {
            category.updateFromDto(updateDto);
        } catch (Exception e) {
            throw new Exception500("카테고리 수정 도중 이상이 생겼습니다.");
        }
    }

    // 카테고리 삭제
    @Transactional
    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception500("카테고리 삭제 도중 이상이 생겼습니다.");
        }
    }


}
