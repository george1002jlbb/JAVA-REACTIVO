package com.carritocompras.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("PRODUCT")
public class Product {
    @Id
    @Column("idProduct")
    private Integer idProduct;
    private String name;
    private String description;
    private String department;
    private String material;
    @Column("imageurl")
    private String imageUrl;
    private double price;
    private int stock;
}
