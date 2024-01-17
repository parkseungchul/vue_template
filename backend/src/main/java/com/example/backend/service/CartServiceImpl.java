package com.example.backend.service;

import com.example.backend.domian.CartEntity;
import com.example.backend.domian.CartRepository;
import com.example.backend.dto.Cart;
import com.example.backend.mapper.CartMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{


    final CartMapper cartMapper;
    final CartRepository cartRepository;
    public CartServiceImpl(CartMapper cartMapper, CartRepository cartRepository){
        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart getCart(int memberId, int itemId) {
        CartEntity cartEntity = cartRepository.findByMemberIdAndItemId(memberId, itemId);
        if(cartEntity != null){
            return cartMapper.toDto(cartEntity);
        }
        return null;
    }

    @Override
    public void saveCart(Cart cart) {

        cartRepository.save(cartMapper.toEntity(cart));
    }

    @Override
    public List<Cart> listCart(int memberId) {
        List<CartEntity> cartEntityList = cartRepository.findByMemberId(memberId);
        /**
        List cartList = new ArrayList<Cart>();
        for(CartEntity cartEntity: cartEntityList){
            cartList.add(cartMapper.toDto(cartEntity));
        }
        return cartList;
        **/
        return cartEntityList.stream()
                .map(cartMapper::toDto)
                .collect(Collectors.toList());
    }
}
