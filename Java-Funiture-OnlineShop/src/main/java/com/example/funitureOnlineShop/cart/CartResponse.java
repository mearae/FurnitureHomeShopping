package com.example.funitureOnlineShop.cart;

import com.example.funitureOnlineShop.option.Option;
import com.example.funitureOnlineShop.product.Product;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {

    @Setter
    @Getter
    @ToString
    public static class FindAllDto {
        List<ProductDto> products;

        private Long totalPricing;

        public FindAllDto(List<Cart> cartList) {
            this.products = cartList.stream()
                    .map(cart -> cart.getOption().getProduct()).distinct()
                    .map(product -> new ProductDto(product, cartList)).collect(Collectors.toList());


            this.totalPricing = cartList.stream()
                    .mapToLong(cart -> (cart.getOption().getProduct().getPrice() + cart.getOption().getPrice()) * cart.getQuantity())
                    .sum();
        }

        @Setter
        @Getter
        public class ProductDto {
            private Long id;

            private String productName;

            private Long deliveryFee;

            List<CartDto> cartDtos;

            public ProductDto(Product product, List<Cart> cartList){
                this.id = product.getId();
                this.productName = product.getProductName();
                this.deliveryFee = product.getDeliveryFee();
                this.cartDtos = cartList.stream()
                        .filter(cart -> cart.getOption().getProduct().getId() == product.getId())
                        .map(CartDto::new).collect(Collectors.toList());
            }

            @Setter
            @Getter
            public class CartDto {
                private Long id;

                private OptionDto optionDto;

                private Long price;

                private Long quantity;

                public CartDto(Cart cart) {
                    this.id = cart.getId();
                    this.optionDto = new OptionDto(cart.getOption());
                    this.price = cart.getPrice();
                    this.quantity = cart.getQuantity();
                }

                @Setter
                @Getter
                public class OptionDto {
                    private Long id;
                    private String optionName;
                    private Long price;

                    public OptionDto(Option option) {
                        this.id = option.getId();
                        this.optionName = option.getOptionName();
                        this.price = option.getPrice();
                    }
                }
            }
        }
    }

    @Data
    public static class UpdateDTO{

        private Long cartId;

        private Long optionId;

        private String optionName;

        private Long quantity;

        private Long price;

        public UpdateDTO(Cart cart) {
            this.cartId = cart.getId();
            this.optionId = cart.getOption().getId();
            this.optionName = cart.getOption().getOptionName();
            this.quantity = cart.getQuantity();
            this.price = cart.getPrice();
        }
    }

    @Data
    public static class DeleteDTO {
        private Long cartId;
        private Long quantity;
    }
}