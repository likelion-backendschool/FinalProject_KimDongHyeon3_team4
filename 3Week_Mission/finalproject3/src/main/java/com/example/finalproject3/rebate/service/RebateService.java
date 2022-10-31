package com.example.finalproject3.rebate.service;


import com.example.finalproject3.order.entity.OrderItem;
import com.example.finalproject3.order.service.OrderService;
import com.example.finalproject3.rebate.entity.RebateOrderItem;
import com.example.finalproject3.rebate.repository.RebateOrderItemRepository;
import com.example.finalproject3.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RebateService {

    private  final OrderService orderService;
    private final RebateOrderItemRepository rebateOrderItemRepository;
    public void createData(String yearMonth) {

        int monthEndDay = Util.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Util.date.parse(fromDateStr);
        LocalDateTime toDate = Util.date.parse(toDateStr);

        List<OrderItem> orderItems = orderService.findByPayDateBetween(fromDate, toDate);

        // 주문된 item들을 정산 아이템으로 변환
        List<RebateOrderItem> rebateOrderItems = orderItems
                .stream()
                .map(this::toAdmRebateOrderItem)
                .collect(Collectors.toList());

        // 변환한 내용 정산 데이터로 변환
        rebateOrderItems.forEach(this::makeAmdRebateOrderItem);
    }

    public void makeAmdRebateOrderItem(RebateOrderItem item) {
        RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItemId(item.getOrderItem().getId()).orElse(null);

        if (oldRebateOrderItem != null) {
            rebateOrderItemRepository.delete(oldRebateOrderItem);
        }

        rebateOrderItemRepository.save(item);
    }

    public RebateOrderItem toAdmRebateOrderItem(OrderItem orderItem) {
        return new RebateOrderItem(orderItem);
    }
}
