package com.example.backend.controller;

import com.example.backend.dto.Item;
import com.example.backend.service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ItemController {

    private ItemService itemService;
    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping("/api/items")
    public List<Item> getItems(){
        return itemService.getItems();

    }
}
