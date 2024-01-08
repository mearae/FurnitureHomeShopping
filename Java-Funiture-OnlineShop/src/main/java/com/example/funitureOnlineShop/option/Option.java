package com.example.funitureOnlineShop.option;

import com.example.funitureOnlineShop.cart.Cart;
import com.example.funitureOnlineShop.order.item.Item;
import com.example.funitureOnlineShop.orderCheck.OrderCheck;
import com.example.funitureOnlineShop.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name ="option_tb",
        indexes = {
                @Index(name = "option_product_id_index", columnList = "product_id")
        })
public class Option {
    // ** PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // ** 옵션 이름
    @Column(length = 100, nullable = false)
    private String optionName;
    // ** 옵션 가격
    private Long price;
    // ** 상품의 재고 수량
    private Long stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @OneToMany(mappedBy = "option", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cart> cart = new ArrayList<>();

    @OneToMany(mappedBy = "option", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderCheck> orderChecks = new ArrayList<>();

    @OneToOne(mappedBy = "option", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Item item;

    @Builder
    public Option(Long id, String optionName, Long price, Long stockQuantity, Product product, List<Cart> cart, List<OrderCheck> orderChecks, Item item) {
        this.id = id;
        this.optionName = optionName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.product = product;
        this.cart = cart;
        this.orderChecks = orderChecks;
        this.item = item;
    }

    public Option toUpdate(Product product) {
        Option option = new Option();
        this.product = product;
        return option;
    }

    public void updateFromDTO(OptionResponse.FindAllDTO optionDTO){

        this.optionName = optionDTO.getOptionName();
        this.price = optionDTO.getPrice();
        this.stockQuantity = optionDTO.getStockQuantity();
    }
    public void updateStock(Long change) {
        this.stockQuantity += change;
    }
}
