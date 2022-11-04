package com.example.finalproject3.revenue.repository;

import com.example.finalproject3.rebate.entity.RebateOrderItem;
import com.example.finalproject3.revenue.entity.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    void deleteByRebateOrderItem(RebateOrderItem oldRebateOrderItem);
}
