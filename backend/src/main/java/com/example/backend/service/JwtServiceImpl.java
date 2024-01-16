package com.example.backend.service;

import com.example.backend.domian.TokenEntiry;
import com.example.backend.domian.TokenRepository;
import com.example.backend.dto.Token;
import com.example.backend.dto.TokenStatus;
import com.example.backend.mapper.TokenMapper;
import io.jsonwebtoken.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtServiceImpl implements JwService {

    private TokenMapper tokenMapper;
    private TokenRepository tokenRepository;

    public JwtServiceImpl(TokenMapper tokenMapper, TokenRepository tokenRepository) {
        this.tokenMapper = tokenMapper;
        this.tokenRepository = tokenRepository;
    }

    private String secretKey = "asdfadsfsdfsdaf!@#$!#%#$YGREHERWGSDAFASAFADSHAHFGRRHGRGQRGEGR";

    @Override
    public void deleteToken(String token) {
        tokenRepository.deleteById(token);
    }

    // 리프레시 토큰 생성 메소드
    private String generateRefreshToken(String token) {
        TokenStatus tokenStatus = getClaims(token);
        if (tokenStatus != null) {
            Object value = tokenStatus.getClaims().get("id");
            // 리프레시 토큰은 더 긴 유효 기간을 가짐 (1초 *60(1분) * 60(1시간) *24(하루) * 180(180일)
            return generateToken("id", value, (1000 * 60 * 60) * 24 * 180);
        }
        return null;
    }


    @Deprecated
    public String generateToken(String key, Object value, long expirationTime) {
        Date expTime = new Date(System.currentTimeMillis() + expirationTime);
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> header = new HashMap<>();
        header.put("type", "JWT");
        header.put("algorithm", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put(key, value);

        return Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setExpiration(expTime)
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateToken(String key, Object value) {
        Key signKey = getSignKey();

        // 토큰 생성을 위한 공통 설정
        Map<String, Object> header = new HashMap<>();
        header.put("type", "JWT");
        header.put("algorithm", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put(key, value);

        Date now = new Date(System.currentTimeMillis());

        // 주 토큰 생성 (유효 기간 1초)
        String token = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + 1000 * 60))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();

        // 리프레시 토큰 생성 (유효 기간 1분)
        String refreshToken = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setExpiration(new Date(now.getTime() + 1000 * 60 * 5))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();

        // 토큰 저장
        Token tokenDto = new Token(token, refreshToken);
        TokenEntiry tokenEntity = tokenMapper.toEntity(tokenDto);
        tokenRepository.save(tokenEntity);

        log.debug("Generated Token: " + token);
        log.debug("Generated Refresh Token: " + refreshToken);

        return token;
    }


    @Override
    @Transactional
    public TokenStatus getClaims(String token) {
        if (token != null && !token.isEmpty()) {
            try {
                Key signKey = getSignKey();

                return new TokenStatus(false, token, Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody());
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token: reissue");
                // 주 토큰 만료 시 리프레시 토큰 검증 및 새 토큰 발급
                TokenEntiry tokenEntity = tokenRepository.findByToken(token);
                if (tokenEntity != null) {
                    String refresh_token = tokenEntity.getRefresh_token();
                    try {
                        // 키 가져와서 재생성
                        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(refresh_token);
                        Claims refresh_claims = claimsJws.getBody();

                        String id = (String) refresh_claims.get("id");
                        String new_token = generateToken("id", id);
                        tokenRepository.deleteById(token);

                        return new TokenStatus(true, new_token, Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(new_token).getBody());
                    } catch (JwtException jwt_e) {
                        // 리프레시 토큰 관련 예외 처리
                        log.error("Error validating refresh token", jwt_e);
                    }
                }
            } catch (Exception e) {
                log.error("Error validating token", e);
            }
        }
        return null;
    }
    // 서명 키 생성 메소드
    private Key getSignKey() {
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
    }
}
