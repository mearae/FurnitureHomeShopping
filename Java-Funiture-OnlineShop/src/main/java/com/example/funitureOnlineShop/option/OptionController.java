package com.example.funitureOnlineShop.option;

import com.example.funitureOnlineShop.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/options")
public class OptionController {
    private final OptionService optionService;

    // ** 옵션 저장
    @PostMapping("/products/{productId}/save")
    public ResponseEntity<?> save(@PathVariable Long productId, @RequestBody @Valid OptionResponse.FindByProductIdDTO requestDTO) {
        // URL 경로에서 추출한 productId를 DTO에 설정
        requestDTO.setProductId(productId);

        System.out.println(requestDTO.getOptionName());
        System.out.println(requestDTO.getPrice());
        System.out.println(requestDTO.getStockQuantity());

        optionService.save(requestDTO);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(apiResult);
    }
    // ** 개별 옵션 검색
    @GetMapping("/products/{id}")
    public ResponseEntity<?> findByid(@PathVariable Long id){
        List<OptionResponse.FindByProductIdDTO> optionResponses = optionService.findByProductId(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }
    // ** 전체 옵션 검색
    @GetMapping("/products")
    public ResponseEntity<?> findAll(){
        List<OptionResponse.FindAllDTO> optionResponses =
                optionService.findAll();
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionResponses);
        return ResponseEntity.ok(apiResult);
    }
    // ** 옵션 변경
    @PostMapping("/update")
    public ResponseEntity<?> update(OptionResponse.FindAllDTO optionDto) {
        optionService.update(optionDto);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(optionDto);
        return ResponseEntity.ok(apiResult);
    }
    // ** 옵션 삭제
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        optionService.delete(id);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(id);
        return ResponseEntity.ok(apiResult);
    }
}
