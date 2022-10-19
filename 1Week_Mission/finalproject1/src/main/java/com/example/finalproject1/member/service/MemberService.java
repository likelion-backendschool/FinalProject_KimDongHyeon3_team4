package com.example.finalproject1.member.service;

import com.example.finalproject1.member.entity.Member;
import com.example.finalproject1.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    public void save(String username, String password, String email, String nickname) {

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .authLevel(3)
                .build();

        memberRepository.save(member);

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setText("가입을 축하합니다.");
        emailMessage.setSubject("서비스 가입을 완료하였습니다.");
        javaMailSender.send(emailMessage);
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
}
