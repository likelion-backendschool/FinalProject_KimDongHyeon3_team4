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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final MemberService memberService;

    public Order createByProduct(Member member, Product product) {

        List<OrderItem> orderItems = new ArrayList<>();

        orderItems.add(OrderItem.builder()
                .product(product)
                .price(product.getPrice())
                .salePrice(product.getSalePrice())
                .wholesalePrice(product.getWholesalePrice())
                .build());

        return save(member, orderItems);
    }

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

        order.makeName();

        orderRepository.save(order);

        return order;
    }

    public void payByTossPayments(Order order) {
        Member buyer = order.getMember();

        int payPrice = order.getPayPrice();

        memberService.addCash(buyer, payPrice, "주문결제충전__토스페이먼츠");

        memberService.addCash(buyer, payPrice*-1, "주문결제__토스페이먼츠");

        order.setPaymentDone();
        orderRepository.save(order);
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

    public void refund(Order order) {

        int payPrice = order.getPayPrice();
        memberService.addCash(order.getMember(), payPrice, "주문환불__예치금환불");

        order.setRefundDone();
        orderRepository.save(order);
    }


    public Order findById(long id) {
        return orderRepository.findById(id).orElse(null);
    }


    public boolean memberCanPayment(Member member, Order order) {
        return member.getId().equals(order.getMember().getId());
    }
}
