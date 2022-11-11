package com.example.finalproject4.restapi.mybook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiProduct {

    Long id;
    LocalDateTime createDate;
    LocalDateTime modifyDate;
    Long authorId;
    String authorName;
    String subject;
    List<ApiPost> bookChapters;
}
