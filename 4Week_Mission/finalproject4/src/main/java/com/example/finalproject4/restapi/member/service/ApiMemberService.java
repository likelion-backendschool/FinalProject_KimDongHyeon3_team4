package com.example.finalproject4.restapi.member.service;

import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiMemberService {

    private final JwtProvider jwtProvider;

    public String genAccessToken(Member member) {
        return jwtProvider.generateAccessToken(member.getAccessTokenClaims(), 60 * 60 * 24 * 90);
    }


}
