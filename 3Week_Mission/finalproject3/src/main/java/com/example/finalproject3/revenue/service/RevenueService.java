package com.example.finalproject3.revenue.service;

import com.example.finalproject3.rebate.entity.RebateOrderItem;
import com.example.finalproject3.revenue.repository.RevenueRepository;
import com.example.finalproject3.revenue.entity.Revenue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final RevenueRepository revenueRepository;

    public void addRevenue(RebateOrderItem rebateOrderItem) {

        Revenue revenue = Revenue.builder()
                .rebateOrderItem(rebateOrderItem)
                .revenue(rebateOrderItem.getSalePrice() - rebateOrderItem.getWholesalePrice())
                .build();

        revenueRepository.save(revenue);
    }

    public List<Revenue> findAll() {
        return revenueRepository.findAll();
    }

    public int getTotalRevenue(){
        List<Revenue> revenues = findAll();

        int totalRevenue = 0 ;

        for(Revenue revenue : revenues){
            totalRevenue += revenue.getRevenue();
        }

        return totalRevenue;
    }

    @Transactional
    public void deleteByRebateOrderItem(RebateOrderItem oldRebateOrderItem) {
        revenueRepository.deleteByRebateOrderItem(oldRebateOrderItem);
    }
}
