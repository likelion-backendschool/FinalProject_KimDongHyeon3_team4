package com.example.finalproject3.member.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class JoinForm {
    
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
    private String nickname;

}
