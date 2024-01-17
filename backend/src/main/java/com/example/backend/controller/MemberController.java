package com.example.backend.controller;

import com.example.backend.dto.Member;
import com.example.backend.dto.TokenStatus;
import com.example.backend.service.JwtService;
import com.example.backend.service.MemberService;
import jakarta.servlet.http.Cookie;
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
    @PostMapping("/api/member/login")
    public ResponseEntity login(@RequestBody Map<String, String> params, HttpServletResponse res) {
        Member member = memberService.login(params.get("email"), params.get("password"));
        if (member != null) {
            int id = member.getId();
            String token = jwService.generateToken("id", id);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            res.addCookie(cookie);
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/api/member/logout")
    public ResponseEntity logout(@CookieValue(value = "token", required = false) String token, HttpServletResponse res) {
        if (token != null) {
            jwService.deleteToken(token);
        }
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/api/member/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token, HttpServletResponse res) {
        TokenStatus tokenStatus = jwService.getClaims(token);
        if (tokenStatus != null) {
            String id = tokenStatus.getClaims().get("id").toString();
            if (tokenStatus.isRefresh()) {
                Cookie cookie = new Cookie("token", tokenStatus.getToken());
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                res.addCookie(cookie);
            }
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
