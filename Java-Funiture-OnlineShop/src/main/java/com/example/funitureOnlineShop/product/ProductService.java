package com.example.funitureOnlineShop.product;

import com.example.funitureOnlineShop.category.Category;
import com.example.funitureOnlineShop.category.CategoryRepository;
import com.example.funitureOnlineShop.core.error.exception.Exception400;
import com.example.funitureOnlineShop.core.error.exception.Exception404;
import com.example.funitureOnlineShop.option.Option;
import com.example.funitureOnlineShop.option.OptionRepository;
import com.example.funitureOnlineShop.productFile.ProductFile;
import com.example.funitureOnlineShop.productFile.ProductFileRepository;
import com.example.funitureOnlineShop.productFile.ProductFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ProductFileRepository productFileRepository;
    private final CategoryRepository categoryRepository;

    private final List<String> isImage = new ArrayList<>(Arrays.asList(
            ".tiff", ".jfif", ".bmp", ".gif", ".svg", ".png", ".jpeg",
            ".svgz", ".webp", ".jpg", ".ico", ".xbm", ".dib", ".pjp",
            ".apng", ".tif", ".pjpeg", "avif"));

    // ------------<파일경로>-------------
    // !!!!!!!!!! 꼭 반드시 테스트시 파일 경로 특히 사용자명 확인할것 !!!!!!!!!!
    private final String filePath = "C:/shoppingFiles/";

    @Transactional
    public Product save(ProductResponse.SaveByIdDTO saveByIdDTO, MultipartFile[] files) throws IOException {
        // categoryId를 사용하여 Category 엔티티를 찾음
        Category category = categoryRepository.findById(saveByIdDTO.getCategoryId())
                .orElseThrow( () -> new Exception404("해당 카테고리가 존재하지 않습니다."));

        // 상품 엔티티 생성 및 카테고리 할당
        Product productEntity = Product.builder()
                .productName(saveByIdDTO.getProductName())
                .description(saveByIdDTO.getDescription())
                .price(saveByIdDTO.getPrice())
                .deliveryFee(saveByIdDTO.getDeliveryFee())
                .category(category) // 찾은 Category 설정
                .build();

        // 상품 엔티티 저장
        Product savedProduct = productRepository.save(productEntity);

        // 파일 처리 로직
        saveFiles(files, savedProduct);

        return savedProduct;
    }

    @Transactional
    public void saveFiles(MultipartFile[] files, Product product) throws IOException {
        if (!files[0].isEmpty()) {
            Path uploadPath = Paths.get(filePath);

            // 만약 경로가 없다면... 경로 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {
                // 파일명 추출
                String originalFilename = file.getOriginalFilename();

                // 확장자 추출
                String formatType = originalFilename.substring(
                        originalFilename.lastIndexOf("."));

                if (!isImage.contains(formatType))
                    throw new Exception400("이미지 파일만 가능합니다.");

                // UUID 생성
                String uuid = UUID.randomUUID().toString();

                // 경로 지정
                String path = filePath + uuid + originalFilename;

                // 파일을 물리적으로 저장 (DB에 저장 X)
                file.transferTo( new File(path) );

                ProductFile productFile = ProductFile.builder()
                        .filePath(filePath)
                        .fileName(originalFilename)
                        .uuid(uuid)
                        .fileType(formatType)
                        .fileSize(file.getSize())
                        .product(product)
                        .build();

                productFileRepository.save(productFile);
            }
        }
    }

    // 상품 수정 서비스
    @Transactional
    public ProductResponse.FindByCategoryDTO update(ProductResponse.UpdateDTO updateDTO, MultipartFile[] files) throws IOException {
        Product product = getProduct(updateDTO.getId());
        Category category = categoryRepository.findById(updateDTO.getCategoryId()).get();

        product.update(updateDTO, category);

        // 수정된 제품에 해당하는 옵션 리스트를 가져옴
        List<Option> optionList = optionRepository.findByProductId(product.getId());

        // 상품 id에 따른 FileProduct를 찾는 코드
        productFileRepository.deleteAllByProductId(updateDTO.getId());
        saveFiles(files, product);

        // 수정된 제품 정보를 FindByIdDTO 객체로 변환하여 반환
        return ProductResponse.FindByCategoryDTO.toDto(product, new ProductFile());
    }

    // 삭제 서비스
    @Transactional
    public void delete(Long id) {
        getProduct(id);
        productRepository.deleteById(id);
    }

    // 상품 전체 찾기 서비스
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new Exception404("등록된 상품이 존재하지 않습니다.");
        }
        return products;
    }

    // 상품 id 찾는 로직
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 상품을 찾을 수 없습니다."));
    }

    // ID로 특정 상품 하나 찾기
    public ProductResponse.FindByIdDTO findById(Long id) {
        Product product = getProduct(id);
        List<Option> optionList = optionRepository.findByProductId(product.getId());

        // 상품 id에 따른 FileProduct들을 찾는 코드
        List<ProductFile> productFileList = productFileRepository.findByProductId(id);

        if (productFileList.isEmpty())
            productFileList.add(new ProductFile());

        return ProductResponse.FindByIdDTO.toDto(product, optionList, productFileList);
    }

    public ProductFileResponse findByIdFile(Long id) {
        Optional<ProductFile> optionalFile = productFileRepository.findById(id);

        if (optionalFile.isEmpty())
            throw new Exception404("해당 파일을 찾을 수 없습니다." + id);

        ProductFile file = optionalFile.get();

        return ProductFileResponse.toDto(file);
    }

    public Page<ProductResponse.FindByCategoryDTO> paging(Long categoryId, Pageable pageable) {
        // ** 페이지 시작 번호
        int page = pageable.getPageNumber() - 1;

        // ** 페이지에 포함될 게시물 개수
        int size = 10;

        Page<Product> products = productRepository.findAllByCategoryId(
                categoryId, PageRequest.of(page, size));
        return products.map(product -> new ProductResponse.FindByCategoryDTO(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                productFileRepository.findByProductId(product.getId())));
    }

    public List<ProductResponse.FindByCategoryDTO> findByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);

        return products.stream().map(product -> ProductResponse.FindByCategoryDTO.toDto(product, new ProductFile())).collect(Collectors.toList());
    }
}