package com.example.backend.domian;

import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<TokenEntiry,String> {

    TokenEntiry findByToken(String token);

    void deleteByToken(String token);
}
