package com.example.funitureOnlineShop.orderCheck;

import com.example.funitureOnlineShop.comment.ProductComment;
import com.example.funitureOnlineShop.option.Option;
import com.example.funitureOnlineShop.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class OrderCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tid;

    private String orderId;

    private Long quantity;

    private Long price;

    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(mappedBy = "orderCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductComment productComment;

    @Builder
    public OrderCheck(Long id, String tid, String orderId, Long quantity, Long price, LocalDateTime orderDate, Option option, User user, ProductComment productComment) {
        this.id = id;
        this.tid = tid;
        this.orderId = orderId;
        this.quantity = quantity;
        this.price = price;
        this.orderDate = orderDate;
        this.option = option;
        this.user = user;
        this.productComment = productComment;
    }

    public static void sortByCreateDate(List<OrderCheck> orderChecks) {
        orderChecks.sort(Comparator.comparing(OrderCheck::getOrderDate).reversed());
    }
}
