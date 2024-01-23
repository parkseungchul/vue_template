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

    // token available time
    private Duration lifeCycle;
    // refresh token available time
    private Duration refreshLifeCycle;
    // DTO to Entity or Entity to DTO
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

    // it is for encryption, so it is never exposed anywhere
    private String secretKey = "asdfadsfsdfsdaf!@#$!#%#$YGREHERWGSDAFASAFADSHAHFGRRHGRGQRGEGR";

    /**
     * It deletes a token
     *
     * @param tokenId
     * @param token
     */
    @Override
    public void deleteToken(Integer tokenId, String token) {
        tokenRepository.deleteByTokenIdAndToken(tokenId, token);
    }

    /**
     * @param orgTokenEntity : If if is null, it should be an Insert Token, otherwise It should be an Update Token
     * @param key
     * @param value
     * @return
     */
    @Override
    public TokenStatus generateToken(TokenEntity orgTokenEntity, String key, Object value) {
        Key signKey = getSignKey();

        // create token for sharing setting
        Map<String, Object> header = new HashMap<>();
        header.put("type", "JWT");
        header.put("algorithm", "HS256");

        Map<String, Object> claims = new HashMap<>();
        claims.put(key, value);

        Instant now = Instant.now();
        Instant expiryDate = now.plus(lifeCycle);
        Instant refreshExpiryDate = now.plus(refreshLifeCycle);

        // create main token
        String token = Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setExpiration(Date.from(expiryDate))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();

        // create refresh token
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
            tokenStatus.setUpdate(true);
        } else {  // newbie token
            tokenStatus.setCreate(true);
        }
        tokenEntity = tokenRepository.save(tokenEntity);
        tokenStatus.setTokenId(tokenEntity.getTokenId());
        tokenStatus.setMemberId((Integer) value);
        tokenStatus.setToken(token);

        return tokenStatus;
    }

    /**
     * It is checked, whether the token is available or not
     * @param tokenId
     * @param token
     * @return
     */
    @Override
    public boolean isValid(int tokenId, String token) {
        return this.getClaims(tokenId, token) != null;
    }

    /**
     * It retrieves the Member-ID from the token.
     * @param tokenId
     * @param token
     * @return
     */
    @Override
    public int getMemberId(int tokenId, String token) {
        TokenStatus tokenStatus = this.getClaims(tokenId, token);
        if (tokenStatus != null) {
            return Integer.parseInt(tokenStatus.getClaims().get("id").toString());
        }
        return -1;
    }

    /**
     * If it is a new or a refresh token, it should update the cookies.
     * @param tokenStatus
     * @param res
     */
    @Override
    public void setCookies(TokenStatus tokenStatus, HttpServletResponse res) {
        log.debug("setCookies " + tokenStatus.toString());
        if (tokenStatus.isCreate() || tokenStatus.isUpdate()) {
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

    /**
     * It is verified whether the token is available or not.
     * however,even if the token is expired, it should retrieve a refresh-token.
     * if the refresh-token is also expired, it should return an error.
     * @param tokenId
     * @param token
     * @return
     */
    @Override
    public TokenStatus getClaims(int tokenId, String token) {
        if (tokenId != 0 && token != null && !token.isEmpty()) {
            try {
                Key signKey = getSignKey();
                Claims claims = Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
                return new TokenStatus(tokenId, Integer.parseInt(claims.get("id").toString()), false, false, token, claims);
            } catch (ExpiredJwtException e) {
                // if token is expired, it should find refresh token.
                log.error("Expired JWT org-token: reissue");
                TokenEntity tokenEntity = tokenRepository.findByTokenIdAndToken(tokenId, token);
                if (tokenEntity != null) {
                    String refresh_token = tokenEntity.getRefreshToken();
                    try {
                        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(refresh_token);
                        Claims refresh_claims = claimsJws.getBody();

                        int id = (Integer) refresh_claims.get("id");
                        TokenStatus tokenStatus = generateToken(tokenEntity, "id", id);
                        return tokenStatus;
                    } catch (JwtException jwt_e) {
                        // even if refresh-token is expired, it should return an error.
                        log.error("Expired JWT refresh-token", jwt_e);
                        tokenRepository.deleteByTokenIdAndToken(tokenId, token);
                    }
                }
            } catch (Exception e) {
                log.error("Error validating token", e);
            }
        }
        return null;
    }

    /**
     * Create a signature key
     * @return
     */
    private Key getSignKey() {
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
    }
}
