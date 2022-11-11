package com.example.finalproject4.withdraw.repository;

import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.withdraw.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {

    List<Withdraw> findByMember(Member member);
}
