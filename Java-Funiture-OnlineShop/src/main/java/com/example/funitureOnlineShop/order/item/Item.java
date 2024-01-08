package com.example.funitureOnlineShop.order.item;

import com.example.funitureOnlineShop.option.Option;
import com.example.funitureOnlineShop.order.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "item_tb",
        indexes = {
                @Index(name = "item_option_id_index", columnList = "option_id"),
                @Index(name = "item_order_id_index", columnList = "order_id")
        })
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    // 장바구니 상품 하나의 가격(배달비 O)
    @Column(nullable = false)
    private Long price;

    @OneToOne(fetch = FetchType.LAZY)
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Builder
    public Item(Long id, Long quantity, Long price, Option option, Order order) {
        this.id = id;
        this.quantity = quantity;
        this.price = price + option.getProduct().getDeliveryFee();
        this.option = option;
        this.order = order;
    }
}
