package com.example.backend.service;

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
    public void getItemService(){
        itemService.getItems().forEach(item -> {
            System.out.println(item.toString());
        });
    }

    @Test
    public void login(){
        int result = memberService.login("test@test.com", "123");
        System.out.println(result);
    }
}
