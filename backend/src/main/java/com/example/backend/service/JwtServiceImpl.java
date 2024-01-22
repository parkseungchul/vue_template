package com.example.backend.service;

import com.example.backend.domian.TokenEntity;
import com.example.backend.domian.TokenRepository;
import com.example.backend.dto.Token;
import com.example.backend.dto.TokenStatus;
import com.example.backend.mapper.TokenMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private Duration lifeCycle;
    private Duration refreshLifeCycle;

    private TokenMapper tokenMapper;
    private TokenRepository tokenRepository;


    public JwtServiceImpl(
            @Value("${custom.token.life-cycle}") String lifeCycle,
            @Value("${custom.token.refresh-life-cycle}") String refreshLifeCycle,
            TokenMapper tokenMapper,
            TokenRepository tokenRepository) {
        this.lifeCycle = Duration.parse(lifeCycle);
        this.refreshLifeCycle = Duration.parse(refreshLifeCycle);
        this.tokenMapper = tokenMapper;
        this.tokenRepository = tokenRepository;
    }

    private String secretKey = "asdfadsfsdfsdaf!@#$!#%#$YGREHERWGSDAFASAFADSHAHFGRRHGRGQRGEGR";

    @Override
    public void deleteToken(Integer tokenId, String token) {
        tokenRepository.deleteByTokenIdAndToken(tokenId, token);
    }


    // 리프레시 토큰 생성 메소드
    @Deprecated
    private String generateRefreshToken(int tokenId, String token) {
        TokenStatus tokenStatus = getClaims(tokenId, token);
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
    public TokenStatus generateToken(TokenEntity orgTokenEntity, String key, Object value) {
        Key signKey = getSignKey();

        // 토큰 생성을 위한 공통 설정
        Map<String, Object> header = new HashMap<>();
        header.put("type", "JWT");
        header.put("algorithm", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put(key, value);

        Instant now = Instant.now();
        Instant expiryDate = now.plus(lifeCycle);
        Instant refreshExpiryDate = now.plus(refreshLifeCycle);


        // 주 토큰 생성 (유효 기간 1분)
        String token = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setExpiration(Date.from(expiryDate))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();

        // 리프레시 토큰 생성 (유효 기간 5분)
        String refreshToken = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setExpiration(Date.from(refreshExpiryDate))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();

        Token tokenDto = new Token(token, refreshToken);
        TokenEntity tokenEntity = tokenMapper.toEntity(tokenDto);


        // return value
        TokenStatus tokenStatus = new TokenStatus();

        // update token
        if (orgTokenEntity != null) {
            tokenEntity.setTokenId(orgTokenEntity.getTokenId());
            tokenStatus.setRefresh(true);
        }else{  // newbie token
            tokenStatus.setRefresh(false);
        }

        tokenEntity = tokenRepository.save(tokenEntity);
        tokenStatus.setTokenId(tokenEntity.getTokenId());
        tokenStatus.setMemberId((Integer)value);
        tokenStatus.setToken(token);

        return tokenStatus;
    }

    @Override
    public boolean isValid(int tokenId, String token) {
        return this.getClaims(tokenId, token) != null;
    }

    @Override
    public int getMemberId(int tokenId, String token) {
        TokenStatus tokenStatus = this.getClaims(tokenId, token);
        if (tokenStatus != null) {
            return Integer.parseInt(tokenStatus.getClaims().get("id").toString());
        }
        return -1;
    }

    @Override
    public void setCookies(TokenStatus tokenStatus, HttpServletResponse res) {
        log.debug("setCookies !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + tokenStatus.toString());
        if(tokenStatus.isRefresh()){
            Cookie tokenCookie = new Cookie("token", tokenStatus.getToken());
            tokenCookie.setHttpOnly(true);
            tokenCookie.setPath("/");
            res.addCookie(tokenCookie);

            Cookie tokenIdCookie = new Cookie("tokenId", String.valueOf(tokenStatus.getTokenId()));
            tokenIdCookie.setHttpOnly(true);
            tokenIdCookie.setPath("/");
            res.addCookie(tokenIdCookie);
        }
    }

    @Override
    public void removeCookies(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        cookie = new Cookie("tokenId", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }


    @Override
    public TokenStatus getClaims(int tokenId, String token) {
        if (tokenId !=0 && token != null && !token.isEmpty()) {
            try {
                Key signKey = getSignKey();
                Claims claims = Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
                return new TokenStatus(tokenId, Integer.parseInt(claims.get("id").toString()), false, token, claims);
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token: reissue");
                // 주 토큰 만료 시 리프레시 토큰 검증 및 새 토큰 발급
                TokenEntity tokenEntity = tokenRepository.findByTokenIdAndToken(tokenId, token);
                if (tokenEntity != null) {
                    String refresh_token = tokenEntity.getRefreshToken();
                    try {
                        // 키 가져와서 재생성
                        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(refresh_token);
                        Claims refresh_claims = claimsJws.getBody();

                        int id = (Integer) refresh_claims.get("id");
                        TokenStatus tokenStatus = generateToken(tokenEntity, "id", id);
                        log.debug("--------------------------------------->"+tokenStatus.toString());
                        return tokenStatus;


                       // return new TokenStatus(tokenStatus.getTokenId(), true, tokenStatus.getToken(), Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(tokenStatus.getToken()).getBody());
                    } catch (JwtException jwt_e) {
                        // 리프레시 토큰 관련 예외 처리
                        // 다쓴 토큰 지우기
//                        tokenRepository.deleteByTokenIdAndToken(tokenId, token);
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
