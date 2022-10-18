package com.example.finalproject1.base;

import com.example.finalproject1.article.entity.Post;
import com.example.finalproject1.article.repository.PostRepository;
import com.example.finalproject1.member.entity.Member;
import com.example.finalproject1.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    CommandLineRunner init(MemberRepository memberRepository, PostRepository postRepository) {
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
        };
    }

}
