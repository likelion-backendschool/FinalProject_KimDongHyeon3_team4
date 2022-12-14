package com.example.finalproject3.hashtag.service;

import com.example.finalproject3.hashtag.entity.HashTag;
import com.example.finalproject3.hashtag.repository.HashTagRepository;
import com.example.finalproject3.keyword.entity.Keyword;
import com.example.finalproject3.keyword.service.KeywordService;
import com.example.finalproject3.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final KeywordService keywordService;
    private final HashTagRepository hashTagRepository;

    public void applyHashTags(Post post, String keywordContentsStr) {

        List<HashTag> oldHashTags = getHashTags(post);

        List<String> keywordContents = Arrays.stream(keywordContentsStr.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<HashTag> needToDelete = new ArrayList<>();

        for (HashTag oldHashTag : oldHashTags) {
            boolean contains = keywordContents.stream().anyMatch(s -> s.equals(oldHashTag.getKeyword().getContent()));

            if (contains == false) {
                needToDelete.add(oldHashTag);
            }
        }

        needToDelete.forEach(hashTag -> {
            hashTagRepository.delete(hashTag);
        });

        keywordContents.forEach(keywordContent -> {
            saveHashTag(post, keywordContent);
        });
    }

    private HashTag saveHashTag(Post post, String keywordContent) {
        Keyword keyword = keywordService.save(keywordContent);

        Optional<HashTag> opHashTag = hashTagRepository.findByPostIdAndKeywordId(post.getId(), keyword.getId());

        if (opHashTag.isPresent()) {
            return opHashTag.get();
        }

        HashTag hashTag = HashTag.builder()
                .post(post)
                .keyword(keyword)
                .build();

        hashTagRepository.save(hashTag);

        return hashTag;
    }

    public List<HashTag> getHashTags(Post post) {
        return hashTagRepository.findAllByPostId(post.getId());
    }

    public List<Keyword> findByPost(Post post) {

        List<HashTag> hashTagRelations = hashTagRepository.findByPost(post);

        List<Keyword> hashtags = new ArrayList<>();

        hashTagRelations.forEach(hashTag -> {
            hashtags.add(hashTag.getKeyword());
        });

        return hashtags;
    }


    public void deleteByPost(Post post) {
        hashTagRepository.deleteByPost(post);
    }
}
