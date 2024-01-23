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
    private boolean isCreate = false;
    private boolean isUpdate = false;
    private String token;
    private Claims claims;

}
