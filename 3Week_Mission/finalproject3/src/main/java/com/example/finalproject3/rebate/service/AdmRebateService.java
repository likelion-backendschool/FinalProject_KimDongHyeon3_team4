package com.example.finalproject3.rebate.service;


import com.example.finalproject3.order.entity.OrderItem;
import com.example.finalproject3.order.service.OrderService;
import com.example.finalproject3.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdmRebateService {

    private  final OrderService orderService;
    public void createData(String yearMonth) {

        int monthEndDay = Util.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Util.date.parse(fromDateStr);
        LocalDateTime toDate = Util.date.parse(toDateStr);

        System.out.println("fromDate = " + fromDate);
        System.out.println("toDate = " + toDate);

        List<OrderItem> orderItems = orderService.findByCreateDateBetween(fromDate, toDate);

        System.out.println("orderItems.size() = " + orderItems.size());
    }
}
