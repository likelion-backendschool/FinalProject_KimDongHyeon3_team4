package com.example.finalproject2.order.service;

import com.example.finalproject2.cart.entity.CartItem;
import com.example.finalproject2.cart.service.CartService;
import com.example.finalproject2.member.entity.Member;
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

    public void createByCart(Member member){

        List<CartItem> cartItems = cartService.findByBuyer(member);

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cartItems){

            Product product = cartItem.getProduct();

            log.info("여기는 ?? = {}", product.getSubject());

            if(product.isOrderable()){
                orderItems.add(OrderItem.builder()
                        .product(product)
                        .build());
            }

            cartService.delete(cartItem);
        }

        for(OrderItem orderItem : orderItems){
            log.info("반복하면은 ? = {}",orderItem.getProduct().getSubject());
        }

        save(member, orderItems);
    }

    private void save(Member member, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .member(member)
//                .orderItems(orderItems)
                .build();

        for(OrderItem orderItem : orderItems){
            log.info("뭐가 나오냐 orderitem아 = {}", orderItem.getProduct().getSubject());
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);
    }
}
