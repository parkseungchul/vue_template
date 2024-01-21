package com.example.backend.mapper;


import com.example.backend.domian.ItemEntity;
import com.example.backend.dto.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toDto(ItemEntity itemEntity);

    ItemEntity toEntity(Item item);
}
