package com.example.finalproject3.post.service;

import com.example.finalproject3.hashtag.service.HashTagService;
import com.example.finalproject3.member.entity.Member;
import com.example.finalproject3.post.entity.Post;
import com.example.finalproject3.post.repository.PostRepository;
import com.example.finalproject3.util.MarkDown;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final HashTagService hashTagService;
    private final MarkDown markDown;
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void save(String subject, String content, Member member) {

        String contentHtml = markDown.markdownWrite(content);

        Post post = Post.builder()
                .author(member)
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .build();

        postRepository.save(post);
    }

    public void save(String subject, String content, Member member, String hashTagStr) {

        String contentHtml = markDown.markdownWrite(content);

        Post post = Post.builder()
                .author(member)
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .build();

        postRepository.save(post);

        hashTagService.applyHashTags(post, hashTagStr);
    }

    public void save(String subject, String content, Post post, String hashTagStr) {

        String contentHtml = markDown.markdownWrite(content);

        post.setSubject(subject);
        post.setContent(content);
        post.setContentHtml(contentHtml);

        hashTagService.applyHashTags(post, hashTagStr);

        postRepository.save(post);
    }

    @Transactional
    public void deleteById(Long id) {

        Optional<Post> optPost = postRepository.findById(id);

        hashTagService.deleteByPost(optPost.get());
        postRepository.deleteById(id);
    }
}
