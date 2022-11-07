package com.example.finalproject4.hashtag.repository;

import com.example.finalproject4.hashtag.entity.HashTag;
import com.example.finalproject4.keyword.entity.Keyword;
import com.example.finalproject4.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {


    Optional<HashTag> findByPostIdAndKeywordId(Long articleId, Long keywordId);

    List<HashTag> findAllByPostId(Long id);

    List<HashTag> findByPost(Post post);

    void deleteByPost(Post post);

    List<HashTag> findByKeyword(Keyword keyword);
}
