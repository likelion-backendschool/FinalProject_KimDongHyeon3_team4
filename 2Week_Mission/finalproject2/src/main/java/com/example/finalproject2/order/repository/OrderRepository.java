package com.example.finalproject2.order.repository;

import com.example.finalproject2.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
