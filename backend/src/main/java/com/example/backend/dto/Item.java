package com.example.backend.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    int id;
    String name;
    private String imgPath;
    private int price;
    private int discountPer;
}
