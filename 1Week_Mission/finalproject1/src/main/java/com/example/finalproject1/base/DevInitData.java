package com.example.finalproject1.base;

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
    CommandLineRunner init(MemberRepository memberRepository) {
        return args -> {
            memberRepository.save(Member.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1234"))
                    .email("sh2115521@naver.com")
                    .nickname("admin")
                    .authLevel(7)
                    .build());

            memberRepository.save(Member.builder()
                    .username("1111")
                    .password(passwordEncoder.encode("1234"))
                    .email("sh2115521@naver.com")
                    .nickname("1234")
                    .authLevel(3)
                    .build());
        };
    }

}
