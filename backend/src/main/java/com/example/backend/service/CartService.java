package com.example.backend.service;

import com.example.backend.dto.Cart;
import com.example.backend.dto.Item;

import java.util.List;

public interface CartService {

    Cart getCart(int memberId, int itemId);

    void delCart(int memberId, int itemId);

    void saveCart(Cart cart);

    List<Item> listItem(int menberId);

}
