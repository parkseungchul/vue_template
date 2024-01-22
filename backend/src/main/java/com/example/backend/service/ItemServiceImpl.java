package com.example.backend.service;

import com.example.backend.domian.ItemRepository;
import com.example.backend.dto.Item;
import com.example.backend.mapper.ItemMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    private ItemRepository itemRepository;
    private ItemMapper itemMapper;
    public ItemServiceImpl(ItemMapper itemMapper, ItemRepository itemRepository){
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }

    /**
     * retrieve whole items
     * @return
     */
    @Override
    public List<Item> getItems() {
        List<Item> items = new ArrayList<Item>();
        itemRepository.findAll().forEach(itemEntity -> {
            items.add(itemMapper.toDto(itemEntity));
        });
        return items;
    }
}
