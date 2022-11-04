package com.example.finalproject3.order.repository;

import com.example.finalproject3.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByPayDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
