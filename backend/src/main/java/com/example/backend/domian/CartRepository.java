package com.example.backend.domian;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends CrudRepository<CartEntity,Integer> {
    List<CartEntity> findByMemberId(int memberId);
    CartEntity findByMemberIdAndItemId(int memberId, int itemId);
    void deleteByMemberIdAndItemId(int memberId, int itemId);


}
