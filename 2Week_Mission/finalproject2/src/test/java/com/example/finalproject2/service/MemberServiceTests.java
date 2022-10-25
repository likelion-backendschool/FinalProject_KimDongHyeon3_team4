package com.example.finalproject2.service;

import com.example.finalproject2.cart.entity.CartItem;
import com.example.finalproject2.cart.service.CartService;
import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.member.repository.MemberRepository;
import com.example.finalproject2.member.service.MemberService;
import com.example.finalproject2.product.entity.Product;
import com.example.finalproject2.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberServiceTests {

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CartService cartService;

    @Test
    @DisplayName("예치금 넣기")
    void t1() {

        Member member = memberService.findByUsername("user1");

        memberService.addCash(member, 5_000, "충전__무통장입금");

        assertThat(member.getRestCash()).isEqualTo(1030000);

        memberService.addCash(member, -5_000, "충전__무통장입금");

        assertThat(member.getRestCash()).isEqualTo(1025000);
    }

    @Test
    @DisplayName("예치금 조회")
    void t2() {

        Member member1 = memberService.findByUsername("user1");
        Member member2 = memberService.findByUsername("user2");

        assertThat(member1.getRestCash()).isEqualTo(1025000);
        assertThat(member2.getRestCash()).isEqualTo(2000000);
    }

}
