package com.example.funitureOnlineShop.cart;

import com.example.funitureOnlineShop.option.Option;
import com.example.funitureOnlineShop.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "cart_tb")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long quantity; // 장바구니의 총수량

    @Column(nullable = false)
    private Long price; //장바구니 총 가격

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩
    private Option option;

    @Builder
    public Cart(Long id, Long quantity, Long price, User user, Option option) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.user = user;
        this.option = option;
    }

    public void update(Long quantity, Long price){
        this.quantity = quantity;
        this.price = price;
    }
}
