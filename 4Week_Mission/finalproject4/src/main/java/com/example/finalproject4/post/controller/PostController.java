package com.example.finalproject4.post.controller;

import com.example.finalproject4.hashtag.service.HashTagService;
import com.example.finalproject4.keyword.entity.Keyword;
import com.example.finalproject4.keyword.service.KeywordService;
import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.member.service.MemberService;
import com.example.finalproject4.post.entity.Post;
import com.example.finalproject4.post.service.PostService;
import com.example.finalproject4.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final KeywordService keywordService;
    private final HashTagService hashTagService;

    @GetMapping("/list")
    public String showPostList(Model model){

        List<Post> posts = postService.findAll();
        List<Keyword> hashtags = keywordService.findAll();

        model.addAttribute("posts",posts);
        model.addAttribute("hashtags",hashtags);

        return "/post/list";
    }

    @GetMapping("/{id}")
    public String showPostDetail(@PathVariable Long id, Model model){

        Post post = postService.findById(id);

        if(post == null){
            return "redirect:/post/list?msg=" + Util.url.encode("해당 글이 존재하지 않습니다.");
        }

        List<Keyword> hashtags = hashTagService.findByPost(post);

        model.addAttribute("post",post);
        model.addAttribute("hashtags",hashtags);

        return "/post/detail";
    }

    @GetMapping("/write")
    public String showPostWrite(){
        return "/post/write";
    }

    @PostMapping("/write")
    public String postWrite(String subject, String content, String tags, Principal principal){

        Member member = memberService.findByUsername(principal.getName());

        principal.getName();
        postService.save(subject, content, member, tags);

        return "redirect:/post/list?msg=" + Util.url.encode("글을 작성하였습니다.");
    }

    @GetMapping("/{id}/delete")
    public String postDelete(@PathVariable Long id){

        postService.deleteById(id);

        return "redirect:/post/list?msg=" + Util.url.encode(id + "글이 삭제되었습니다.");
    }

    @GetMapping("/{id}/modify")
    public String showPostModify(@PathVariable Long id, Model model){

        Post post = postService.findById(id);

        if(post == null){
            return "redirect:/post/list?msg=" + Util.url.encode("해당 글이 존재하지 않습니다.");
        }

        List<Keyword> hashtags = hashTagService.findByPost(post);

        model.addAttribute("post", post);
        model.addAttribute("hashtags",hashtags);

        return "/post/modify";
    }

    @PostMapping("/{id}/modify")
    public String postModify(@PathVariable Long id, String subject, String content, String tags){

        Post post = postService.findById(id);

        if(post == null){
            return "redirect:/post/list?msg=" + Util.url.encode("해당 글이 존재하지 않습니다.");
        }

        postService.save(subject, content, post, tags);

        return "redirect:/post/" + id;
    }
}
