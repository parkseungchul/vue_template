package com.example.backend.service;

import com.example.backend.domian.MemberEntity;
import com.example.backend.domian.MemberRepository;
import com.example.backend.dto.Member;
import com.example.backend.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    private MemberMapper memberMapper;
    private MemberRepository memberRepository;

    public MemberServiceImpl(MemberMapper memberMapper, MemberRepository memberRepository) {
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
    }

    @Override
    public Member login(String email, String password) {
        log.debug(email + "   " + password);
        int result = 0;
        MemberEntity memberEntity = memberRepository.findByEmailAndPassword(email, password);
        if (memberEntity == null) {
            return null;
        }else{
            return memberMapper.toDto(memberEntity);
        }
    }
}
