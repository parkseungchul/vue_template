package com.example.backend.service;

import com.example.backend.dto.Member;

public interface MemberService {

    public Member login(String email, String password);

}
