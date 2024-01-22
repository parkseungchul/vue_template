package com.example.backend.domian;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Tokens")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tokenId;

    @Column(length = 255, nullable = false)
    private String token;

    @Column(length = 255, nullable = false)
    private String refreshToken;

    @Column(name = "createTime", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "updateTime")
    private LocalDateTime updateTime;

    // @PrePersist is executed before the insert operation.
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createTime = now;
        updateTime = now;
    }

    // @PreUpdate is executed before the Update operation.
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
