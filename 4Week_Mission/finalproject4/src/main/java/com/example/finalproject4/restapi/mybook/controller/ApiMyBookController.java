package com.example.finalproject4.restapi.mybook.controller;

import com.example.finalproject4.base.dto.RsData;
import com.example.finalproject4.hashtag.entity.HashTag;
import com.example.finalproject4.hashtag.service.HashTagService;
import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.mybook.entity.MyBook;
import com.example.finalproject4.mybook.service.MyBookService;
import com.example.finalproject4.post.entity.Post;
import com.example.finalproject4.post.service.PostService;
import com.example.finalproject4.restapi.mybook.dto.ApiMyBook;
import com.example.finalproject4.security.dto.SecurityMember;
import com.example.finalproject4.util.Util;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/myBooks")
@RequiredArgsConstructor
@Tag(name = "MyBookController", description = "회원이 구매한 도서 리스트 조회, 회원이 구매한 도서 상세 조회 기능을 구현한 컨트롤러")
public class ApiMyBookController {

    private final MyBookService myBookService;
    private final HashTagService hashTagService;
    private final PostService postService;

    @GetMapping("")
    public ResponseEntity<RsData> showMyBookList(@Parameter(hidden = true) @AuthenticationPrincipal SecurityMember securityMember){

        Member member = securityMember.getMember();

        List<MyBook> myBooks = myBookService.findByMember(member);

        List<ApiMyBook> apiMyBooks = ApiMyBook.getApiMyBookByMyBook(myBooks);

        return Util.spring.responseEntityOf(
                RsData.successOf(
                        Util.mapOf("myBooks",apiMyBooks)
                )
        );
    }

    @GetMapping("/{myBookId}")
    public ResponseEntity<RsData> showMyBookDetail(@Parameter(hidden = true) @AuthenticationPrincipal SecurityMember securityMember,
                                                   @PathVariable Long myBookId){

        //1. 해당 선택한 북을 가져온다.
        Member member = securityMember.getMember();

        MyBook myBook = myBookService.findById(myBookId);

        List<HashTag> hashTags = hashTagService.findByKeyword(myBook.getProduct().getKeyword());

        Member author = myBook.getProduct().getAuthor();

        List<Post> posts = new ArrayList<>();

        for(HashTag hashTag : hashTags){
            if(hashTag.getPost().getAuthor().equals(author))
                posts.add(hashTag.getPost());
        }

        ApiMyBook apiMyBook = ApiMyBook.getApiMyBookByMyBook(myBook, posts);

        return Util.spring.responseEntityOf(
                RsData.successOf(
                        Util.mapOf("myBook",apiMyBook)
                )
        );
    }



}
