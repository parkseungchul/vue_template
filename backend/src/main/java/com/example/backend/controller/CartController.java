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

import java.util.List;

@Slf4j
@RestController
public class CartController {
    // '@Autowired' 대신 'final' 사용하는 것이 멋집니다.
    // 의존성에 'final' 사용하면 그들을 변경할 수 없도록 보장하고 객체의 안전성과 신뢰성을 향상시킵니다.
    // it is awesome to use 'final' than '@Autowired'
    // use 'final' for dependencies ensure they immutable and enhance the safety and reliability of the object.
    private final JwtService jwtService;
    private final CartService cartService;
    public CartController(JwtService jwtService, CartService cartService) {
        this.jwtService = jwtService;
        this.cartService = cartService;
    }

    /**
     * it returns items that user choose ( GetMapping )
     * it requires authority because it retrieves items in the cart. ( throwOnUnauthorized = true )
     * @param req
     * @param res
     * @return
     */
    @GetMapping("/api/cart/items")
    @AuthenticatedEndpoint(throwOnUnauthorized = true)
    public ResponseEntity<List<Item>> getCartItems(
            HttpServletRequest req,
            HttpServletResponse res) {
        log.debug("getCartItems");
        TokenStatus tokenStatus = (TokenStatus)req.getAttribute("tokenStatus");
        int memberId = tokenStatus.getMemberId();
        List<Item> itemList = cartService.listItem(memberId);
        return new ResponseEntity<List<Item>>(itemList, HttpStatus.OK);
    }

    /**
     * Add item to user's cart. if it is already existed, if shouldn't save in DB.
     * It requires authority because it adds the item to the cart.
     * @param itemId
     * @param req
     * @param res
     * @return
     */
    @PostMapping("api/cart/items/{itemId}")
    @AuthenticatedEndpoint(throwOnUnauthorized = true)
    public ResponseEntity putCartItem(
            @PathVariable("itemId") int itemId,
            HttpServletRequest req,
            HttpServletResponse res) {
        log.debug("putCartItem");
        TokenStatus tokenStatus = (TokenStatus)req.getAttribute("tokenStatus");
        int memberId = tokenStatus.getMemberId();
        Cart cart = cartService.getCart(memberId, itemId);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            cartService.saveCart(newCart);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete item from the cart.
     * it requires authority because it deletes the item from the cart.
     * @param itemId
     * @param req
     * @param res
     * @return
     */
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