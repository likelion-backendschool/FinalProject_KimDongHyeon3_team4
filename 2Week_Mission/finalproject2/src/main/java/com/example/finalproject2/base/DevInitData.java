package com.example.finalproject2.base;

import com.example.finalproject2.keyword.entity.Keyword;
import com.example.finalproject2.keyword.service.KeywordService;
import com.example.finalproject2.member.entity.Member;
import com.example.finalproject2.member.repository.MemberRepository;
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
@Profile("dev")
@RequiredArgsConstructor
public class DevInitData {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner init(MemberRepository memberRepository,
                           PostRepository postRepository,
                           PostService postService,
                           KeywordService keywordService,
                           ProductRepository productRepository) {
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

            postRepository.save(Post.builder()
                    .author(member1)
                    .subject("제목1")
                    .content("**내용1**")
                    .contentHtml("내용html1")
                    .build());

            postRepository.save(Post.builder()
                    .author(member2)
                    .subject("제목2")
                    .content("**내용2**")
                    .contentHtml("내용html2")
                    .build());

           postService.save("제목3","내용3", member1, "#자바 #프로그래밍");
           postService.save("제목4","내용4", member2, "#파이썬 #프로그래밍");

           Keyword keyword1 = keywordService.findByContent("자바");
           Keyword keyword2 = keywordService.findByContent("파이썬");

           productRepository.save(Product.builder()
                           .author(member1)
                           .keyword(keyword1)
                           .subject("도서1")
                           .price(100)
                           .build());

            productRepository.save(Product.builder()
                    .author(member2)
                    .keyword(keyword2)
                    .subject("도서2")
                    .price(100)
                    .build());
        };
    }

}
