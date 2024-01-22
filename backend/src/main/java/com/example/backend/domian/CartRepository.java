package com.example.backend.domian;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * The CrudRepository interface is used for CRUD operations on a specific table
 */
public interface CartRepository extends CrudRepository<CartEntity,Integer> {
    List<CartEntity> findByMemberId(int memberId);
    CartEntity findByMemberIdAndItemId(int memberId, int itemId);
    void deleteByMemberIdAndItemId(int memberId, int itemId);


}
