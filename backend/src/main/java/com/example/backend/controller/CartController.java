package com.example.backend.controller;

import com.example.backend.annotation.AuthenticatedEndpoint;
import com.example.backend.dto.Cart;
import com.example.backend.dto.Item;
import com.example.backend.dto.TokenStatus;
import com.example.backend.service.CartService;
import com.example.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
public class CartController {
    private final JwtService jwtService;
    private final CartService cartService;

    public CartController(JwtService jwtService, CartService cartService) {
        this.jwtService = jwtService;
        this.cartService = cartService;
    }
    @GetMapping("/api/cart/items")
    @AuthenticatedEndpoint(throwOnUnauthorized = true)
    public ResponseEntity<List<Item>> getCartItem(
            HttpServletRequest req,
            HttpServletResponse res) {
        log.debug("getCartItem !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        TokenStatus tokenStatus = (TokenStatus)req.getAttribute("tokenStatus");
        int memberId = tokenStatus.getMemberId();
        List<Item> itemList = cartService.listItem(memberId);
        return new ResponseEntity<List<Item>>(itemList, HttpStatus.OK);
    }

    @PostMapping("api/cart/items/{itemId}")
    @AuthenticatedEndpoint(throwOnUnauthorized = true)
    public ResponseEntity pushCartItem(
            @PathVariable("itemId") int itemId,
            HttpServletRequest req,
            HttpServletResponse res) {
        log.debug("pushCartItem1 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        TokenStatus tokenStatus = (TokenStatus)req.getAttribute("tokenStatus");
        int memberId = tokenStatus.getMemberId();
        Cart cart = cartService.getCart(memberId, itemId);
        log.debug("pushCartItem2 "+ cart +"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (cart == null) {

            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            cartService.saveCart(newCart);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("api/cart/items/{itemId}")
    @AuthenticatedEndpoint(throwOnUnauthorized = true)
    public ResponseEntity deleteCartItem(
            @PathVariable("itemId") int itemId,
            HttpServletRequest req,
            HttpServletResponse res) {

        TokenStatus tokenStatus = (TokenStatus)req.getAttribute("tokenStatus");
        int memberId = tokenStatus.getMemberId();
        log.debug("deleteCartItem memberId: "+memberId+" itemId:"  +itemId + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        cartService.delCart(memberId, itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}