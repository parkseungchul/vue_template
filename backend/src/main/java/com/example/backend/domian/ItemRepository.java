package com.example.backend.domian;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<ItemEntity,Integer> {
}
