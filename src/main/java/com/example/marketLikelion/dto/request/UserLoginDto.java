package com.example.marketLikelion.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginDto {
    private String username;
    private String password;
}
