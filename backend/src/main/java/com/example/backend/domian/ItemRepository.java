package com.example.backend.domian;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<ItemEntity,Integer> {

    List<ItemEntity> findByIdIn(List<Integer> idList);
}
