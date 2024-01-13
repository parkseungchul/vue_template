package com.example.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestItemService {

    @Autowired
    ItemService itemService;


    @Test
    public void getItemService(){
        itemService.getItems().forEach(item -> {
            System.out.println(item.toString());
        });


    }
}
