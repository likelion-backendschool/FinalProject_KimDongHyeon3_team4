package com.example.finalproject4.order.repository;

import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMember(Member member);
}
