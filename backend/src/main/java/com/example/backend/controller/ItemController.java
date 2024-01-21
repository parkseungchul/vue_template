package com.example.backend.controller;

import com.example.backend.annotation.AuthenticatedEndpoint;
import com.example.backend.dto.Item;
import com.example.backend.dto.TokenStatus;
import com.example.backend.service.ItemService;
import com.example.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ItemController {
    private JwtService jwtService;
    private ItemService itemService;

    public ItemController(JwtService jwtService, ItemService itemService) {
        this.jwtService = jwtService;
        this.itemService = itemService;
    }

    @GetMapping("/api/items")
    @AuthenticatedEndpoint(throwOnUnauthorized = false)
    public List<Item> getItems(HttpServletRequest req, HttpServletResponse res) {
        log.debug("getItems !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return itemService.getItems();
    }
}
