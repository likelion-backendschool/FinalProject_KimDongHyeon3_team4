package com.example.finalproject2.service;

import com.example.finalproject2.cart.entity.CartItem;
import com.example.finalproject2.cart.repository.CartItemRepository;
import com.example.finalproject2.cart.service.CartService;
import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.member.repository.MemberRepository;
import com.example.finalproject2.product.entity.Product;
import com.example.finalproject2.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CartServiceTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartService cartService;

    @Test
    @DisplayName("장바구니에 담기")
    void t1() {
        Member buyer = memberRepository.findByUsername("user1");

        Product product1 = productService.findById(1L);
        Product product2 = productService.findById(2L);
        Product product3 = productService.findById(3L);
        Product product4 = productService.findById(4L);

        CartItem cartItem1 = cartService.addItem(buyer, product1);
        CartItem cartItem2 = cartService.addItem(buyer, product2);
        CartItem cartItem3 = cartService.addItem(buyer, product3);
        CartItem cartItem4 = cartService.addItem(buyer, product4);

        assertThat(cartItem1).isNotNull();
        assertThat(cartItem2).isNotNull();
        assertThat(cartItem3).isNotNull();
        assertThat(cartItem4).isNotNull();
    }

    @Test
    @DisplayName("장바구니에서 제거")
    void t2() {
        Member buyer1 = memberRepository.findByUsername("user1");
        Member buyer2 = memberRepository.findByUsername("user2");

        Product product1 = productService.findById(1L);
        Product product2 = productService.findById(2L);
        Product product3 = productService.findById(3L);
        Product product4 = productService.findById(4L);

        cartService.removeItem(buyer1, product1);
        cartService.removeItem(buyer1, product2);
        cartService.removeItem(buyer2, product3);
        cartService.removeItem(buyer2, product4);

        assertThat(cartService.hasItem(buyer1, product1)).isFalse();
        assertThat(cartService.hasItem(buyer1, product1)).isFalse();
        assertThat(cartService.hasItem(buyer1, product1)).isFalse();
        assertThat(cartService.hasItem(buyer1, product1)).isFalse();
    }
}