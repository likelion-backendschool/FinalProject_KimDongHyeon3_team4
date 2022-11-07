package com.example.finalproject4.restapi.member.dto;


import com.example.finalproject4.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiMember {

    Long id;
    LocalDateTime createDate;
    LocalDateTime modifyDate;
    String username;
    String email;
    boolean emailVerified;
    String nickname;

    public static ApiMember getApiMemberByMember(Member member) {

        ApiMember apiMember = ApiMember.builder()
                .id(member.getId())
                .createDate(member.getCreateDate())
                .modifyDate(member.getModifyDate())
                .username(member.getUsername())
                .email(member.getEmail())
                .emailVerified(true)
                .nickname(member.getNickname())
                .build();

        return apiMember;
    }
}
