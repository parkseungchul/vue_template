package com.example.backend.service;

import com.example.backend.domian.TokenRepository;
import com.example.backend.dto.TokenStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtServiceImplTest {

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    TokenRepository tokenRepository;

    @Test
    public void testTokenValidity() throws InterruptedException {

    }
}
