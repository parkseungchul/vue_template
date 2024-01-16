package com.example.backend.service;

import com.example.backend.dto.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestService {

    @Autowired
    ItemService itemService;

    @Autowired
    MemberService memberService;

    @Test
    public void getItemService() {
        itemService.getItems().forEach(item -> {
            System.out.println(item.toString());
        });
    }

    @Test
    public void login() {
        Member member = memberService.login("test@test.com", "123");

    }
}
