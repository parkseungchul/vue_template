package com.example.backend.mapper;

import com.example.backend.domian.CartEntity;
import com.example.backend.dto.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * It is a library for mapping between Java objects.
 */
@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toDto(CartEntity cartEntity);

    @Mapping(target = "id", ignore = true)
    CartEntity toEntity(Cart cart);
}
