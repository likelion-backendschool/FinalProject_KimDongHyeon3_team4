package com.example.finalproject1.article.service;

import com.example.finalproject1.article.entity.Post;
import com.example.finalproject1.article.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
