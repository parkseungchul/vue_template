package com.example.backend.domian;

import org.springframework.data.repository.CrudRepository;
public interface MemberRepository extends CrudRepository<MemberEntity,Integer> {
    MemberEntity findByEmailAndPassword(String email, String password);
}
