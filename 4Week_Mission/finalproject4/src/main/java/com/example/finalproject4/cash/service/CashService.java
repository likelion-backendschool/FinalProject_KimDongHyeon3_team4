package com.example.finalproject4.cash.service;

import com.example.finalproject4.cash.entity.CashLog;
import com.example.finalproject4.cash.repository.CashLogRepository;
import com.example.finalproject4.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashService {

    private final CashLogRepository cashLogRepository;

    public CashLog addCash(Member member, long changePrice, String eventType){
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(changePrice)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        return cashLog;
    }

    public long getRestCash(Member member) {
        return member.getRestCash();
    }
}
