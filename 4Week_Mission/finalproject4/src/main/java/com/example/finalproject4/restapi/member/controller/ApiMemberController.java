package com.example.finalproject4.restapi.member.controller;


import com.example.finalproject4.base.dto.RsData;
import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.member.service.MemberService;
import com.example.finalproject4.restapi.member.service.ApiMemberService;
import com.example.finalproject4.security.dto.SecurityMember;
import com.example.finalproject4.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
public class ApiMemberController {

    private final MemberService memberService;
    private final ApiMemberService apiMemberService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<RsData> showLogin(String username, String password){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication", "JWT_Access_Token");

        log.info("username = {}", username);

        Member member = memberService.findByUsername(username);

        if(member == null){
            return Util.spring.responseEntityOf(RsData.of("F-2", "일치하는 회원이 존재하지 않습니다."));
        }

        if(passwordEncoder.matches(password, member.getPassword()) == false){
            return Util.spring.responseEntityOf(RsData.of("F-3", "비밀번호가 일치하지 않습니다."));
        }

        log.debug("Util.json.toStr(member.getAccessTokenClaims()) : " + Util.json.toStr(member.getAccessTokenClaims()));

        String accessToken = apiMemberService.genAccessToken(member);

        String body = "username : %s, password : %s".formatted(member.getUsername(), member.getPassword());

        return Util.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다.",
                        Util.mapOf(
                                "accessToken", accessToken
                        )
                ),
                Util.spring.httpHeadersOf("Authentication", accessToken)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<RsData> me(@AuthenticationPrincipal SecurityMember securityMember) {
//        if (securityMember == null) { // 임시코드, 나중에는 시프링 시큐리티를 이용해서 로그인을 안했다면, 아예 여기로 못 들어오도록
//            return Util.spring.responseEntityOf(RsData.failOf(null));
//        }

        return Util.spring.responseEntityOf(RsData.successOf("성공성공"));
    }
}
