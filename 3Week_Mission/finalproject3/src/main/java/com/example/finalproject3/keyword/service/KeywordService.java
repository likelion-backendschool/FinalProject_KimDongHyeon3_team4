package com.example.finalproject3.keyword.service;

import com.example.finalproject3.keyword.entity.Keyword;
import com.example.finalproject3.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public Keyword save(String keywordContent) {
        Optional<Keyword> optKeyword = keywordRepository.findByContent(keywordContent);

        if ( optKeyword.isPresent() ) {
            return optKeyword.get();
        }

        Keyword keyword = Keyword
                .builder()
                .content(keywordContent)
                .build();

        keywordRepository.save(keyword);

        return keyword;
    }

    public List<Keyword> findAll() {
        return keywordRepository.findAll();
    }

    public Keyword findByContent(String content) {
        return keywordRepository.findByContent(content).orElse(null);
    }
}
