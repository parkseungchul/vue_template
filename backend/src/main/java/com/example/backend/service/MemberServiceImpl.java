package com.example.backend.service;

import com.example.backend.domian.MemberEntity;
import com.example.backend.domian.MemberRepository;
import com.example.backend.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class MemberServiceImpl implements MemberService{

    private MemberMapper memberMapper;
    private MemberRepository memberRepository;


    public MemberServiceImpl(MemberMapper memberMapper, MemberRepository memberRepository){
        this.memberMapper = memberMapper;
        this.memberRepository = memberRepository;
    }

    @Override
    public int login(String email, String password) {
        log.debug(email + "   " + password);
        MemberEntity memberEntity = memberRepository.findByEmailAndPassword(email, password);
        if(memberEntity == null){
            return 0;
        }else{
            return memberEntity.getId();
        }
    }
}
