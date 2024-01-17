package com.example.backend.mapper;


import com.example.backend.domian.ItemEntity;
import com.example.backend.dto.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(source = "id", target = "id_dto")
    @Mapping(source = "name", target = "name_dto")
    Item toDto(ItemEntity itemEntity);
    @Mapping(source = "id_dto", target = "id")
    @Mapping(source = "name_dto", target = "name")
    ItemEntity toEntity(Item item);
}
