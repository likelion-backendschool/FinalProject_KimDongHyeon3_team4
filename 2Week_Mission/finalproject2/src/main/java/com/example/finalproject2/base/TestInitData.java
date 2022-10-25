package com.example.finalproject2.base;

import com.example.finalproject2.cart.service.CartService;
import com.example.finalproject2.keyword.entity.Keyword;
import com.example.finalproject2.keyword.service.KeywordService;
import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.member.repository.MemberRepository;
import com.example.finalproject2.member.service.MemberService;
import com.example.finalproject2.order.service.OrderService;
import com.example.finalproject2.post.entity.Post;
import com.example.finalproject2.post.repository.PostRepository;
import com.example.finalproject2.post.service.PostService;
import com.example.finalproject2.product.entity.Product;
import com.example.finalproject2.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TestInitData {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner init(MemberRepository memberRepository,
                           MemberService memberService,
                           PostRepository postRepository,
                           PostService postService,
                           KeywordService keywordService,
                           ProductRepository productRepository,
                           CartService cartService,
                           OrderService orderService) {
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

            postService.save("제목1","내용1", member1, "#C언어 #블로그");
            postService.save("제목2","내용2", member2, "#인프라 #뭐할까");
            postService.save("제목3","내용3", member1, "#자바 #배고파");
            postService.save("제목4","내용4", member2, "#파이썬 #프로그래밍");

            Keyword keyword1 = keywordService.findByContent("자바");
            Keyword keyword2 = keywordService.findByContent("파이썬");
            Keyword keyword3 = keywordService.findByContent("C언어");
            Keyword keyword4 = keywordService.findByContent("프로그래밍");
            Keyword keyword5 = keywordService.findByContent("배고파");
            Keyword keyword6 = keywordService.findByContent("블로그");

            productRepository.save(Product.builder()
                    .author(member1)
                    .keyword(keyword1)
                    .subject("도서1")
                    .price(100)
                    .fee(10)
                    .build());

            productRepository.save(Product.builder()
                    .author(member2)
                    .keyword(keyword2)
                    .subject("도서2")
                    .price(200)
                    .fee(20)
                    .build());

            productRepository.save(Product.builder()
                    .author(member1)
                    .keyword(keyword3)
                    .subject("도서3")
                    .price(300)
                    .fee(30)
                    .build());

            productRepository.save(Product.builder()
                    .author(member2)
                    .keyword(keyword4)
                    .subject("도서4")
                    .price(400)
                    .fee(40)
                    .build());

            productRepository.save(Product.builder()
                    .author(member2)
                    .keyword(keyword5)
                    .subject("도서5")
                    .price(500)
                    .fee(50)
                    .build());

            productRepository.save(Product.builder()
                    .author(member2)
                    .keyword(keyword6)
                    .subject("도서6")
                    .price(600)
                    .fee(60)
                    .build());

            Product product1 = productRepository.findById(1L).orElse(null);
            Product product2 = productRepository.findById(2L).orElse(null);
            Product product3 = productRepository.findById(3L).orElse(null);
            Product product4 = productRepository.findById(4L).orElse(null);

            cartService.save(member1, product2);
            cartService.save(member1, product4);
            cartService.save(member2, product1);
            cartService.save(member2, product3);

            memberService.addCash(member1, 10_000, "충전__무통장입금");
            memberService.addCash(member1, 20_000, "충전__무통장입금");
            memberService.addCash(member1, -5_000, "출금__일반");
            memberService.addCash(member1, 1_000_000, "충전__무통장입금");
            memberService.addCash(member2, 2_000_000, "충전__무통장입금");

            orderService.createByCart(member1);
        };
    }

}
