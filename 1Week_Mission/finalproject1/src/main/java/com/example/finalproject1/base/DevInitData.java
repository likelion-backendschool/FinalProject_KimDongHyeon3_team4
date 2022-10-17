package com.example.finalproject1.base;

import com.example.finalproject1.member.entity.Member;
import com.example.finalproject1.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevInitData {

    @Bean
    CommandLineRunner init(MemberRepository memberRepository) {
        return args -> {
            memberRepository.save(Member.builder()
                            .username("admin")
                            .password("1234")
                            .email("sh2115521@naver.com")
                            .nickname("admin")
                            .authLevel(7)
                            .build()
            );
        };
    }

}
