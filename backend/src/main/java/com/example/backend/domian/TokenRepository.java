package com.example.backend.domian;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<TokenEntity,String> {

    TokenEntity findByTokenIdAndToken(int tokenId, String token);

    @Transactional
    void deleteByTokenIdAndToken(Integer tokenId, String token);
}
