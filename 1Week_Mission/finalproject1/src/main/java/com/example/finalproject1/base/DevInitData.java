package com.example.finalproject1.base;

import com.example.finalproject1.post.entity.Post;
import com.example.finalproject1.post.repository.PostRepository;
import com.example.finalproject1.member.entity.Member;
import com.example.finalproject1.member.repository.MemberRepository;
import com.example.finalproject1.post.service.PostService;
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
    CommandLineRunner init(MemberRepository memberRepository, PostRepository postRepository, PostService postService) {
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
                    .subject("??????1")
                    .content("**??????1**")
                    .contentHtml("??????html1")
                    .build());

            postRepository.save(Post.builder()
                    .author(member2)
                    .subject("??????2")
                    .content("**??????2**")
                    .contentHtml("??????html2")
                    .build());

           postService.save("??????3","??????3", member1, "#?????? #???????????????");
           postService.save("??????4","??????4", member2, "#????????? #???????????????");
        };
    }

}
