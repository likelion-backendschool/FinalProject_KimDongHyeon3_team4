package com.example.finalproject2.order.repository;

import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMember(Member member);
}
