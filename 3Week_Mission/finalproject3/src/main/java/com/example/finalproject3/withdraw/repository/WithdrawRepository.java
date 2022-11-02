package com.example.finalproject3.withdraw.repository;

import com.example.finalproject3.member.entity.Member;
import com.example.finalproject3.withdraw.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {

    List<Withdraw> findByMember(Member member);
}
