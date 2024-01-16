package com.example.backend.domian;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Tokens")
public class TokenEntiry {
    @Id
    @Column(length = 255, nullable = false)
    private String token;

    @Column(length = 255, nullable = false)
    private String refresh_token;

}
