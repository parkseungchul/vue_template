package com.example.backend.service;

import com.example.backend.dto.Item;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface MemberService {

    public int login(String email, String password) throws ResponseStatusException;

}
