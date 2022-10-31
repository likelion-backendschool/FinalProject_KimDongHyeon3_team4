package com.example.finalproject3.cart.repository;

import com.example.finalproject3.cart.entity.CartItem;
import com.example.finalproject3.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByBuyerIdAndProductId(Long buyerId, Long productId);

    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);

    List<CartItem> findByBuyer(Member buyer);
}
