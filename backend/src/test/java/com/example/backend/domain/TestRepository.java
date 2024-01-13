package com.example.backend.domain;

import com.example.backend.controller.ItemController;
import com.example.backend.domian.ItemRepository;
import com.example.backend.dto.Item;
import com.example.backend.mapper.ItemMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRepository {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemMapper itemMapper;

    @Test
    public void itemCRUD(){

        Item item = new Item();
        item.setName_dto("first");

        itemRepository.save(itemMapper.toEntity(item));


        itemRepository.findAll().forEach(i-> {
            System.out.println(i.toString());
        });



    }
}
