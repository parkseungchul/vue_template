package com.example.backend.controller;

import com.example.backend.dto.Cart;
import com.example.backend.service.CartService;
import com.example.backend.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CartController {
    private final JwtService jwtService;
    private final CartService cartService;
    public CartController(JwtService jwtService, CartService cartService) {
        this.jwtService = jwtService;
        this.cartService = cartService;
    }

    @GetMapping("/api/cart/items")
    public ResponseEntity<List<Cart>> getCartItem(@CookieValue(value = "token", required = false) String token){
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        List<Cart> cartList = cartService.listCart(memberId);

        return new ResponseEntity<List<Cart>>(cartList, HttpStatus.OK);
    }

    @PostMapping("api/cart/items/{itemId}")
    public ResponseEntity pushCartItem(
            @PathVariable("itemId") int itemId,
            @CookieValue(value = "token", required = false) String token)
    {
        if(!jwtService.isValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        int memberId = jwtService.getId(token);
        if(memberId == -1){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Cart cart = cartService.getCart(memberId, itemId);
        if(cart == null){
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            cartService.saveCart(newCart);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }
}