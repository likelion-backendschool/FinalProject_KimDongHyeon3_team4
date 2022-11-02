package com.example.finalproject3.withdraw.service;


import com.example.finalproject3.member.entity.Member;
import com.example.finalproject3.withdraw.entity.Withdraw;
import com.example.finalproject3.withdraw.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawService {

    private final WithdrawRepository withdrawRepository;

    public void save(String bankName, String bankAccountNo, int price, Member member) {


        Withdraw withdraw = Withdraw.builder()
                .member(member)
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .price(price)
                .build();

        withdrawRepository.save(withdraw);
    }
}
