package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO (Data Transfer Object) is used for transferring data
 * , while an Entity is used for database handling.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    int memberId;
    int itemId;
}
