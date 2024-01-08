package com.example.funitureOnlineShop.orderCheck;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderCheckRepository extends JpaRepository<OrderCheck, Long> {
    List<OrderCheck> findAllByUserId(Long id);

    List<OrderCheck> findAllByTid(String tid);

    void deleteAllByTid(String tid);
}
