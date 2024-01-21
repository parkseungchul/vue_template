package com.example.backend.mapper;


import com.example.backend.domian.TokenEntity;
import com.example.backend.dto.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    Token toDto(TokenEntity tokenEntity);
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    TokenEntity toEntity(Token token);
}
