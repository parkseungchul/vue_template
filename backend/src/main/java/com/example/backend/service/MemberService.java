package com.example.backend.service;

import com.example.backend.dto.Item;
import com.example.backend.dto.Member;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface MemberService {

    public Member login(String email, String password);

}
