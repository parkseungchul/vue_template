package com.example.backend.controller;

import com.example.backend.annotation.AuthenticatedEndpoint;
import com.example.backend.dto.Member;
import com.example.backend.dto.TokenStatus;
import com.example.backend.service.JwtService;
import com.example.backend.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@RestController
public class MemberController {
    private JwtService jwService;
    private MemberService memberService;

    public MemberController(JwtService jwService, MemberService memberService) {
        this.jwService = jwService;
        this.memberService = memberService;
    }

    /**
     * login action
     *
     * @param params
     * @param res
     * @return
     */
    @PostMapping("/api/member/login")
    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
        Member member = memberService.login(params.get("email"), params.get("password"));
        log.debug("login");
        if (member != null) {
            int id = member.getId();
            TokenStatus tokenStatus = jwService.generateToken(null, "id", id);
            jwService.setCookies(tokenStatus, res);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    /**
     * logout action
     *
     * @param tokenId
     * @param token
     * @param res
     * @return
     */
    @PostMapping("/api/member/logout")
    public ResponseEntity logout(
            @CookieValue(value = "tokenId", required = false) Integer tokenId,
            @CookieValue(value = "token", required = false) String token,
            HttpServletResponse res) {
        log.debug("logout");
        if (token != null && tokenId != null) {
            jwService.deleteToken(tokenId, token);
        }
        jwService.removeCookies(res);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * check available token.
     * if can be an available token or not.
     * if it is available token, it should return the ID of member.
     *
     * @param req
     * @param res
     * @return
     */
    @GetMapping("/api/member/check")
    @AuthenticatedEndpoint(throwOnUnauthorized = false)
    public ResponseEntity check(
            HttpServletRequest req,
            HttpServletResponse res) {
        log.debug("check");
        TokenStatus tokenStatus = (TokenStatus) req.getAttribute("tokenStatus");
        if (tokenStatus == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            int memberId = tokenStatus.getMemberId();
            return new ResponseEntity<>(memberId, HttpStatus.OK);
        }
    }
}
