package com.example.backend.service;

import com.example.backend.dto.TokenStatus;
import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JwService {

    public TokenStatus getClaims(String token);

    public void deleteToken(String token);

    public String generateToken(String key, Object value);
}
