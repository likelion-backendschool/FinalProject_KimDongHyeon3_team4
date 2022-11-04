package com.example.finalproject3.withdraw.service;


import com.example.finalproject3.base.dto.RsData;
import com.example.finalproject3.cash.entity.CashLog;
import com.example.finalproject3.member.entity.Member;
import com.example.finalproject3.member.service.MemberService;
import com.example.finalproject3.withdraw.entity.Withdraw;
import com.example.finalproject3.withdraw.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;
    private final MemberService memberService;

    public void save(String bankName, String bankAccountNo, int price, Member member) {

        Withdraw withdraw = Withdraw.builder()
                .member(member)
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .price(price)
                .isWithdraw(false)
                .build();

        withdrawRepository.save(withdraw);
    }

    public List<Withdraw> findByMember(Member member) {
        return withdrawRepository.findByMember(member);
    }

    public List<Withdraw> findAll() {
        return withdrawRepository.findAll();
    }

    @Transactional
    public RsData withdrawApply(long withdrawApplyId) {

        Withdraw withdraw = withdrawRepository.findById(withdrawApplyId).orElse(null);

        if(withdraw == null || !withdraw.withdrawAvailable()){
            return RsData.of("F-2", "출금11을 할 수 없는 상태입니다.");
        }

        CashLog cashLog = memberService.withdrawCash(
                withdraw.getMember(),
                withdraw.getPrice()*-1,
                "출금__%d__예치금".formatted(withdraw.getMember().getId())
        ).getData().getCashLog();

        //여기서 출금을 했다면, 해당 예치금을 줄이고 그 금액을 사용자에게 이체해주어야 한다.
        //여기서는 오픈뱅크 api를 활용해 이체를 해야한다.

        withdraw.withdrawDone();

        return RsData.of("S-1", "%s님이 신청하신 %d 원이 출금되었습니다.".formatted(withdraw.getMember().getUsername(), withdraw.getPrice()));
    }
}
