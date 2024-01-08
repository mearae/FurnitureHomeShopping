package com.example.funitureOnlineShop.order;

import com.example.funitureOnlineShop.core.security.CustomUserDetails;
import com.example.funitureOnlineShop.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<?> save (@AuthenticationPrincipal CustomUserDetails customUserDetails){
        OrderResponse.FindByIdDTO findByIdDTO = orderService.save(customUserDetails.getUser());
        return ResponseEntity.ok(ApiUtils.success(findByIdDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        OrderResponse.FindByIdDTO findByIdDTO = orderService.findById(id);

        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(findByIdDTO);
        return ResponseEntity.ok(ApiUtils.success(apiResult));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        orderService.delete(id, null);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(null);
        return ResponseEntity.ok(ApiUtils.success(apiResult));
    }
}
