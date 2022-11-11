package com.example.finalproject4.revenue.service;

import com.example.finalproject4.rebate.entity.RebateOrderItem;
import com.example.finalproject4.revenue.entity.Revenue;
import com.example.finalproject4.revenue.repository.RevenueRepository;
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
