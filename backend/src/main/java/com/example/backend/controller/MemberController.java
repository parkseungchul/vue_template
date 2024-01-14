package com.example.backend.controller;

import com.example.backend.domian.MemberRepository;
import com.example.backend.dto.Item;
import com.example.backend.service.ItemService;
import com.example.backend.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class MemberController {

    private MemberService memberService;
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping("/api/member/login")
    public int login(
            @RequestBody Map<String, String> params
    ){
        return memberService.login(params.get("email"), params.get("password"));
    }
}
