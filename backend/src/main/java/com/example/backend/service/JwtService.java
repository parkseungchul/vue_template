package com.example.backend.service;

import com.example.backend.domian.TokenEntity;
import com.example.backend.dto.TokenStatus;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtService {
    public TokenStatus getClaims(int tokenId, String token);
    public void deleteToken(Integer tokenId, String token);
    public TokenStatus generateToken(TokenEntity tokenEntity, String key, Object value);
    boolean isValid(int tokenId, String token);
    int getMemberId(int tokenId, String token);
    void setCookies(TokenStatus tokenStatus, HttpServletResponse res);

    void removeCookies(HttpServletResponse res);
}
