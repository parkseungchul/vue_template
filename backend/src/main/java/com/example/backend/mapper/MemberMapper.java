package com.example.backend.mapper;


import com.example.backend.domian.MemberEntity;
import com.example.backend.dto.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    //@Mapping(target = "id", ignore = true) // doesn't need it.
    //@Mapping(source = "email", target = "email")
    //@Mapping(source = "password", target = "password")
    Member toDto(MemberEntity MemberEntity);
    //@Mapping(target = "id", ignore = true)
    MemberEntity toEntity(Member Member);
}
