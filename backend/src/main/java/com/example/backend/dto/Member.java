package com.example.backend.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    public Member(String email, String password){
        this.email = email;
        this.password = password;
    }
    private int id;
    private String email;
    private String password;

}
