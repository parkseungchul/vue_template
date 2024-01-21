package com.example.backend.dto;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenStatus {
    private int tokenId;
    private int memberId;
    private boolean isRefresh = false;
    private String token;
    private Claims claims;

}
