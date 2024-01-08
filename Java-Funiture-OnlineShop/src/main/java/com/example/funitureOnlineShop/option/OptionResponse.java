package com.example.funitureOnlineShop.option;

import com.example.funitureOnlineShop.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class OptionResponse {
    private Long id;

    private Long productId;

    private Product product;

    private String optionName;

    private Long price;

    private Long stockQuantity;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class FindByProductIdDTO {
        private Long id;

        private Long productId;

        private String optionName;

        private Long price;

        private Long stockQuantity;

        public FindByProductIdDTO(Option option) {
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.stockQuantity = option.getStockQuantity();
        }

        public Option toEntity() {
            return Option.builder()
                    .optionName(optionName)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .build();
        }
    }
    @NoArgsConstructor
    @Getter
    @Setter
    public static class FindAllDTO{
        private Long id;

        private Long productId;

        private String optionName;

        private Long price;

        private Long stockQuantity;

        public FindAllDTO(Option option){
            this.id = option.getId();
            this.productId = option.getProduct().getId();
            this.optionName = option.getOptionName();
            this.price = option.getPrice();
            this.stockQuantity = option.getStockQuantity();
        }

        public Option toEntity(){
            return Option.builder()
                    .optionName(optionName)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .build();
        }
    }
}
