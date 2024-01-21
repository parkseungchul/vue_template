package com.example.backend.domain;

import com.example.backend.controller.ItemController;
import com.example.backend.domian.*;
import com.example.backend.dto.Cart;
import com.example.backend.dto.Item;
import com.example.backend.dto.Member;
import com.example.backend.mapper.CartMapper;
import com.example.backend.mapper.ItemMapper;
import com.example.backend.mapper.MemberMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<ItemEntity> itemList = new ArrayList<ItemEntity>();
        Item item1 = Item.builder().id(0).name("bar").imgPath("/img/bar.jpg").price(10000).discountPer(10).build();
        Item item2 = Item.builder().id(0).name("self").imgPath("/img/self.jpg").price(20000).discountPer(20).build();
        Item item3 = Item.builder().id(0).name("sunflower").imgPath("/img/sunflower.jpg").price(30000).discountPer(30).build();

        itemList.add(itemMapper.toEntity(item1));
        itemList.add(itemMapper.toEntity(item2));
        itemList.add(itemMapper.toEntity(item3));

        List<Integer> integerList = ((List<ItemEntity>) itemRepository.saveAll(itemList)).stream().map(ItemEntity::getId).collect(Collectors.toList());
        itemRepository.findByIdIn(integerList).forEach(itemEntity -> {
            System.out.println(itemEntity.toString());
        });
    }

    @Test
    public void memberCRUD(){

        memberRepository.save(memberMapper.toEntity(new Member("test@test.com", "123")));

        MemberEntity memberEntity = memberRepository.findByEmailAndPassword("test@test.com", "123");
        if(memberEntity != null){
            System.out.println(memberEntity.toString());
            System.out.println(memberMapper.toDto(memberEntity).toString());
        }
    }


    @Test
    @Transactional
    public void cartCRUD(){
        Cart cart = new Cart(1,1);
        cartRepository.save(cartMapper.toEntity(cart));



    }
}
