package com.example.finalproject4.restapi.mybook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiPost {
    Long id;
    String subject;
    String content;
    String contentHtml;
}
