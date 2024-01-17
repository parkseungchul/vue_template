package com.example.backend.domain;

import com.example.backend.controller.ItemController;
import com.example.backend.domian.CartRepository;
import com.example.backend.domian.ItemRepository;
import com.example.backend.domian.MemberEntity;
import com.example.backend.domian.MemberRepository;
import com.example.backend.dto.Cart;
import com.example.backend.dto.Item;
import com.example.backend.mapper.CartMapper;
import com.example.backend.mapper.ItemMapper;
import com.example.backend.mapper.MemberMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRepository {

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartRepository cartRepository;

    @Test
    public void itemCRUD(){
        Item item = new Item();
        item.setName_dto("first");
        itemRepository.save(itemMapper.toEntity(item));
        itemRepository.findAll().forEach(i-> {
            System.out.println(i.toString());
        });

    }

    @Test
    public void memberCRUD(){
        MemberEntity memberEntity = memberRepository.findByEmailAndPassword("test@test.com", "123");

        if(memberEntity != null){
            System.out.println(memberEntity.toString());

            System.out.println(memberMapper.toDto(memberEntity).toString());
        }
    }


    @Test
    public void cartCRUD(){
        Cart cart = new Cart(1,1);
        cartRepository.save(cartMapper.toEntity(cart));



    }
}
