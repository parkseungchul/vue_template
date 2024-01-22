package com.example.backend.service;

import com.example.backend.domian.CartEntity;
import com.example.backend.domian.CartRepository;
import com.example.backend.domian.ItemRepository;
import com.example.backend.dto.Cart;
import com.example.backend.dto.Item;
import com.example.backend.mapper.CartMapper;
import com.example.backend.mapper.ItemMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{

    final CartMapper cartMapper;
    final CartRepository cartRepository;
    final ItemMapper itemMapper;
    final ItemRepository itemRepository;

    public CartServiceImpl(CartMapper cartMapper, CartRepository cartRepository, ItemMapper itemMapper, ItemRepository itemRepository){
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.cartMapper = cartMapper;
        this.cartRepository = cartRepository;
    }

    /**
     * It checks for the existence of the item in the cart by its Member-ID and Item-ID.
     * @param memberId
     * @param itemId
     * @return
     */
    @Override
    public Cart getCart(int memberId, int itemId) {
        CartEntity cartEntity = cartRepository.findByMemberIdAndItemId(memberId, itemId);
        if(cartEntity != null){
            return cartMapper.toDto(cartEntity);
        }
        return null;
    }

    /**
     * It deletes the item in the cart by its Member-ID and Item-ID
     * @param memberId
     * @param itemId
     */
    @Override
    @Transactional
    public void delCart(int memberId, int itemId) {
        cartRepository.deleteByMemberIdAndItemId(memberId, itemId);
    }

    /**
     * It saves the cart information, including the Member-ID.
     * @param cart
     */
    @Override
    public void saveCart(Cart cart) {
        cartRepository.save(cartMapper.toEntity(cart));
    }

    /**
     * It retrieves items in the cart by its Member-ID
     * @param memberId
     * @return
     */
    @Override
    public List<Item> listItem(int memberId) {
        List<CartEntity> cartEntityList = cartRepository.findByMemberId(memberId);
        List<Integer> itemIdList = cartEntityList.stream().map(CartEntity::getItemId).collect(Collectors.toList());

        List<Item> itemList = new ArrayList<Item>();
        itemRepository.findByIdIn(itemIdList).forEach(itemEntity -> {
            itemList.add(itemMapper.toDto(itemEntity));
        });

        return itemList;
    }
}
