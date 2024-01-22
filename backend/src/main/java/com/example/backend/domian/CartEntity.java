package com.example.backend.domian;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The @Entity annotation is used in the Java Persistence API. (must need @Id, can use @Column, @GeneratedValue, etc)
 * The @Table annotation is used to map an entity class to a database table in object-relational mapping
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Carts")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int memberId;

    @Column
    private int itemId;
}
