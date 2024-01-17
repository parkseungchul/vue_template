package com.example.backend.mapper;


import com.example.backend.domian.TokenEntiry;
import com.example.backend.dto.Token;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenMapper {

    Token toDto(TokenEntiry tokenEntiry);
    TokenEntiry toEntity(Token token);
}
