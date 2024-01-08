package com.example.funitureOnlineShop.order;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderRequest {

    private Long id;

    private Long userId;

    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        private Long optionId;
        private Long quantity;

        public OrderItemRequest(Long optionId, Long quantity) {
            this.optionId = optionId;
            this.quantity = quantity;
        }
    }

    @Getter
    @Setter
    public static class OrderDTO{
        private Long id;

        private Long userId;

        public OrderDTO(){
            Order order = new Order();
            this.id = order.getId();
            this.userId = order.getUser().getId();
        }
    }
}