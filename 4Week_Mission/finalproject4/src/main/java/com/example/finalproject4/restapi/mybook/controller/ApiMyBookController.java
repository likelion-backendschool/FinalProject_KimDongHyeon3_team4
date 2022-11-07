package com.example.finalproject4.restapi.mybook.controller;

import com.example.finalproject4.base.dto.RsData;
import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.mybook.entity.MyBook;
import com.example.finalproject4.mybook.service.MyBookService;
import com.example.finalproject4.restapi.mybook.dto.ApiMyBook;
import com.example.finalproject4.security.dto.SecurityMember;
import com.example.finalproject4.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/myBooks")
@RequiredArgsConstructor
public class ApiMyBookController {

    private final MyBookService myBookService;

    @GetMapping("")
    public ResponseEntity<RsData> showMyBookList(@AuthenticationPrincipal SecurityMember securityMember){

        Member member = securityMember.getMember();

        List<MyBook> myBooks = myBookService.findByMember(member);

        List<ApiMyBook> apiMyBooks = ApiMyBook.getApiMyBookByMyBook(myBooks);

        return Util.spring.responseEntityOf(
                RsData.successOf(
                        Util.mapOf("myBooks",apiMyBooks)
                )
        );
    }

}
