package com.example.finalproject3.rebate.repository;

import com.example.finalproject3.rebate.entity.RebateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RebateOrderItemRepository extends JpaRepository<RebateOrderItem, Long> {
    Optional<RebateOrderItem> findByOrderItemId(Long id);

    List<RebateOrderItem> findByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate);
}
