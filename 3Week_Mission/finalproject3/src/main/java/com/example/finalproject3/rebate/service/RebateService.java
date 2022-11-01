package com.example.finalproject3.rebate.service;


import com.example.finalproject3.base.dto.RsData;
import com.example.finalproject3.cash.entity.CashLog;
import com.example.finalproject3.member.service.MemberService;
import com.example.finalproject3.order.entity.OrderItem;
import com.example.finalproject3.order.service.OrderService;
import com.example.finalproject3.rebate.entity.RebateOrderItem;
import com.example.finalproject3.rebate.repository.RebateOrderItemRepository;
import com.example.finalproject3.revenue.service.RevenueService;
import com.example.finalproject3.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RebateService {

    private final OrderService orderService;
    private final MemberService memberService;
    private final RevenueService revenueService;
    private final RebateOrderItemRepository rebateOrderItemRepository;
    public RsData createData(String yearMonth) {

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

        return RsData.of("S-1", "정산데이터가 성공적으로 생성되었습니다.");
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

    public List<RebateOrderItem> findByPayDateIn(String yearMonth) {
        int monthEndDay = Util.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Util.date.parse(fromDateStr);
        LocalDateTime toDate = Util.date.parse(toDateStr);

        return rebateOrderItemRepository.findByPayDateBetweenOrderByIdAsc(fromDate, toDate);
    }

    @Transactional
    public RsData rebate(long orderItemId) {
        RebateOrderItem rebateOrderItem = rebateOrderItemRepository.findByOrderItemId(orderItemId).get();

        if (rebateOrderItem.isRebateAvailable() == false) {
            return RsData.of("F-1", "정산을 할 수 없는 상태입니다.");
        }

        int calculateRebatePrice = rebateOrderItem.getRebatePrice();

        CashLog cashLog = memberService.addCashRebate(
                rebateOrderItem.getProduct().getAuthor(),
                calculateRebatePrice,
                "정산__%d__지급__예치금".formatted(rebateOrderItem.getOrderItem().getId())
        ).getData().getCashLog();

        rebateOrderItem.setRebateDone(cashLog.getId());

        revenueService.addRevenue(rebateOrderItem);

        return RsData.of(
                "S-1",
                "주문품목번호 %d번에 대해서 판매자에게 %s원 정산을 완료하였습니다.".formatted(rebateOrderItem.getOrderItem().getId(), calculateRebatePrice),
                Util.mapOf(
                        "cashLogId", cashLog.getId()
                )
        );
    }
}
