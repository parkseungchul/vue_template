package com.example.backend.service;

import com.example.backend.dto.TokenStatus;

public interface JwtService {
    public TokenStatus getClaims(String token);
    public void deleteToken(String token);
    public String generateToken(String key, Object value);
    boolean isValid(String token);
    int getId(String token);
}
