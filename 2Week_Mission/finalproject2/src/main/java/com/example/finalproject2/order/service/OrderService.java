package com.example.finalproject2.order.service;

import com.example.finalproject2.cart.entity.CartItem;
import com.example.finalproject2.cart.service.CartService;
import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.member.service.MemberService;
import com.example.finalproject2.order.entity.Order;
import com.example.finalproject2.order.entity.OrderItem;
import com.example.finalproject2.order.repository.OrderRepository;
import com.example.finalproject2.product.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final MemberService memberService;

    public Order createByCart(Member member){

        List<CartItem> cartItems = cartService.findByBuyer(member);

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cartItems){

            Product product = cartItem.getProduct();

            if(product.isOrderable()){
                orderItems.add(OrderItem.builder()
                        .product(product)
                        .price(product.getPrice())
                        .salePrice(product.getSalePrice())
                        .wholesalePrice(product.getWholesalePrice())
                        .build());
            }

            cartService.delete(cartItem);
        }

        return save(member, orderItems);
    }

    private Order save(Member member, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .member(member)
                .build();

        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);

        return order;
    }

    public void payByRestCash(Order order) {
        Member buyer = order.getMember();

        long restCash = buyer.getRestCash();

        int payPrice = order.getPayPrice();

        if(payPrice > restCash){
            throw new RuntimeException("예치금이 부족합니다.");
        }

        memberService.addCash(buyer, payPrice*-1, "주문결제__예치금결제");

        order.setPaymentDone();
        orderRepository.save(order);
    }

}
