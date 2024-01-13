package com.example.backend.service;

import com.example.backend.dto.Item;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ItemService {

    public List<Item> getItems();

}
