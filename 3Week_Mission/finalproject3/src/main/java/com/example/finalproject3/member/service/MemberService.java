package com.example.finalproject3.member.service;

import com.example.finalproject3.base.dto.RsData;
import com.example.finalproject3.cash.entity.CashLog;
import com.example.finalproject3.cash.service.CashService;
import com.example.finalproject3.member.entity.Member;
import com.example.finalproject3.member.repository.MemberRepository;
import com.example.finalproject3.util.Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final CashService cashService;
    public void save(String username, String password, String email, String nickname) {

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .authLevel(3)
                .build();

        memberRepository.save(member);

        sendEmail(email, "[e-book market] 가입을 축하합니다.","서비스 가입을 완료하였습니다.");
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

    public void verifyEmail(String email, String authKey) {
        sendEmail(email, "[e-book market] 이메일 인증 코드",authKey);
    }

    public void sendEmail(String email, String subject, String content){
        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setText(content);
        emailMessage.setSubject(subject);
        javaMailSender.send(emailMessage);
    }

    @Transactional
    public long addCash(Member member, long changePrice, String eventType) {

        long newRestCash = member.getRestCash() + cashService.addCash(member, changePrice, eventType).getPrice();

        member.setRestCash(newRestCash);
        memberRepository.save(member);

        return newRestCash;
    }

    @Transactional
    public RsData<AddCashRsDataBody> addCashRebate(Member member, long price, String eventType) {

        CashLog cashLog = cashService.addCash(member, price, eventType);

        long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);

        return RsData.of(
                "S-1",
                "성공",
                new AddCashRsDataBody(cashLog, newRestCash)
        );
    }

    @Transactional
    public RsData<AddCashRsDataBody> withdrawCash(Member member, long price, String eventType) {

        CashLog cashLog = cashService.addCash(member, price, eventType);

        long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);

        return RsData.of(
                "S-2",
                "성공",
                new AddCashRsDataBody(cashLog, newRestCash)
        );
    }

    @Data
    @AllArgsConstructor
    public static class AddCashRsDataBody {
        CashLog cashLog;
        long newRestCash;
    }

    public long getRestCash(Member member) {
        member = memberRepository.findById(member.getId()).orElse(null);

        return member.getRestCash();
    }
}
