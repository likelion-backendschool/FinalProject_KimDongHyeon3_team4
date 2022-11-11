package com.example.finalproject4.revenue.repository;

import com.example.finalproject4.rebate.entity.RebateOrderItem;
import com.example.finalproject4.revenue.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    void deleteByRebateOrderItem(RebateOrderItem oldRebateOrderItem);
}
