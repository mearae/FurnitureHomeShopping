package com.example.funitureOnlineShop.order;

import com.example.funitureOnlineShop.order.item.Item;
import com.example.funitureOnlineShop.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "order_tb",
        indexes = {
                @Index(name = "order_user_id_index", columnList = "user_id")
        })
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Item> orderItems = new ArrayList<>();

    @Builder
    public Order(Long id, LocalDateTime orderDate, User user, List<Item> orderItems) {
        this.id = id;
        this.orderDate = orderDate;
        this.user = user;
        this.orderItems = orderItems;
    }
}
