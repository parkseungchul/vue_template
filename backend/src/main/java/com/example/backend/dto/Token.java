package com.example.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    public Token(String token, String refreshToken){
        this.token = token;
        this.refreshToken = refreshToken;
    }

    private int tokenId;
    private String token;
    private String refreshToken;

}
