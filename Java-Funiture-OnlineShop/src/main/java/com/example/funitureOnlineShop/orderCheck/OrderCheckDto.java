package com.example.funitureOnlineShop.orderCheck;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCheckDto {
    // PK
    private Long id;
    // 거래 id
    private String tid;
    private String orderId;
    // 수량
    private Long quantity;
    // 상품명
    private String productName;
    // 옵션명
    private String optionName;
    // 가격
    private Long price;
    // 주문일자
    private LocalDateTime orderDate;
    // 옵션 id
    private Long optionId;
    // 회원 id
    private Long userId;
    // 상품 후기 적힘?
    private Long commentId;

    public static OrderCheckDto toOrderCheckDto(OrderCheck orderCheck, Long commentId) {
        return new OrderCheckDto(orderCheck.getId(),
                orderCheck.getTid(),
                orderCheck.getOrderId(),
                orderCheck.getQuantity(),
                orderCheck.getOption().getProduct().getProductName(),
                orderCheck.getOption().getOptionName(),
                orderCheck.getPrice(),
                orderCheck.getOrderDate(),
                orderCheck.getOption().getId(),
                orderCheck.getUser().getId(),
                commentId);
    }

    public OrderCheck toEntity(){
        return OrderCheck.builder()
                .tid(tid)
                .orderId(orderId)
                .quantity(quantity)
                .price(price)
                .orderDate(orderDate)
                .build();
    }
}
