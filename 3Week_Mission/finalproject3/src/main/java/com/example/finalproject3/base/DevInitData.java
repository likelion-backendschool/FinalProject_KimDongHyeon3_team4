package com.example.finalproject3.base;

import com.example.finalproject3.cart.service.CartService;
import com.example.finalproject3.keyword.entity.Keyword;
import com.example.finalproject3.keyword.service.KeywordService;
import com.example.finalproject3.member.entity.Member;
import com.example.finalproject3.member.repository.MemberRepository;
import com.example.finalproject3.member.service.MemberService;
import com.example.finalproject3.order.entity.Order;
import com.example.finalproject3.order.service.OrderService;
import com.example.finalproject3.post.repository.PostRepository;
import com.example.finalproject3.post.service.PostService;
import com.example.finalproject3.product.entity.Product;
import com.example.finalproject3.product.repository.ProductRepository;
import com.example.finalproject3.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DevInitData {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner init(MemberRepository memberRepository,
                           MemberService memberService,
                           PostRepository postRepository,
                           PostService postService,
                           KeywordService keywordService,
                           ProductRepository productRepository,
                           CartService cartService,
                           OrderService orderService,
                           WithdrawService withdrawService) {
        return args -> {
            memberRepository.save(Member.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1111"))
                    .email("admin@naver.com")
                    .nickname("admin")
                    .authLevel(7)
                    .build());

            memberRepository.save(Member.builder()
                    .username("user1")
                    .password(passwordEncoder.encode("1111"))
                    .email("sh2115521@naver.com")
                    .nickname("user1")
                    .authLevel(3)
                    .build());

            memberRepository.save(Member.builder()
                    .username("user2")
                    .password(passwordEncoder.encode("1111"))
                    .email("sh2115522@naver.com")
                    .nickname("user2")
                    .authLevel(3)
                    .build());

            Member member1 = memberRepository.findByUsername("user1");
            Member member2 = memberRepository.findByUsername("user2");

           postService.save("??????1","??????1", member1, "#C?????? #?????????");
           postService.save("??????2","??????2", member2, "#????????? #?????????");
           postService.save("??????3","??????3", member1, "#?????? #?????????");
           postService.save("??????4","??????4", member2, "#????????? #???????????????");

           Keyword keyword1 = keywordService.findByContent("??????");
           Keyword keyword2 = keywordService.findByContent("?????????");
           Keyword keyword3 = keywordService.findByContent("C??????");
           Keyword keyword4 = keywordService.findByContent("???????????????");
           Keyword keyword5 = keywordService.findByContent("?????????");
           Keyword keyword6 = keywordService.findByContent("?????????");

           productRepository.save(Product.builder()
                           .author(member1)
                           .keyword(keyword1)
                           .subject("??????1")
                           .price((int) (100*1.6))
                           .wholesalePrice((int) (100*0.9))
                           .salePrice(100)
                           .build());

            productRepository.save(Product.builder()
                    .author(member2)
                    .keyword(keyword2)
                    .subject("??????2")
                    .price((int) (200*1.6))
                    .wholesalePrice((int) (200*0.9))
                    .salePrice(200)
                    .build());

            productRepository.save(Product.builder()
                    .author(member1)
                    .keyword(keyword3)
                    .subject("??????3")
                    .price((int) (300*1.6))
                    .wholesalePrice((int) (300*0.9))
                    .salePrice(300)
                    .build());

            productRepository.save(Product.builder()
                    .author(member2)
                    .keyword(keyword4)
                    .subject("??????4")
                    .price((int) (400*1.6))
                    .wholesalePrice((int) (400*0.9))
                    .salePrice(400)
                    .build());

            productRepository.save(Product.builder()
                    .author(member2)
                    .keyword(keyword5)
                    .subject("??????5")
                    .price((int) (500*1.6))
                    .wholesalePrice((int) (500*0.9))
                    .salePrice(500)
                    .build());

            productRepository.save(Product.builder()
                    .author(member2)
                    .keyword(keyword6)
                    .subject("??????6")
                    .price((int) (600*1.6))
                    .wholesalePrice((int) (600*0.9))
                    .salePrice(600)
                    .build());

            Product product1 = productRepository.findById(1L).orElse(null);
            Product product2 = productRepository.findById(2L).orElse(null);
            Product product3 = productRepository.findById(3L).orElse(null);
            Product product4 = productRepository.findById(4L).orElse(null);

            cartService.save(member1, product2);
            cartService.save(member1, product4);

            cartService.save(member2, product1);
            cartService.save(member2, product3);

            memberService.addCash(member1, 10_000, "??????__???????????????");
            memberService.addCash(member1, 20_000, "??????__???????????????");
            memberService.addCash(member1, -5_000, "??????__??????");
            memberService.addCash(member1, 1_000_000, "??????__???????????????");
            memberService.addCash(member2, 2_000_000, "??????__???????????????");

            Order order1 = orderService.createByCart(member1);
            Order order2 = orderService.createByCart(member2);

            orderService.payByRestCash(order1);    //?????? ?????? ?????? ??????????????? ?????? ??????
            orderService.payByRestCash(order2);
            orderService.refund(order1);



            withdrawService.save("????????????","1002156314175", 5000, member2);
            withdrawService.save("????????????","1002156314175", 5000, member1);

        };
    }

}
