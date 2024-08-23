package com.carritocompras.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ITEMORDER")
public class ItemOrder {
    @Id
    @Column("idItemOrder")
    private Integer idItemOrder;
    @Column("idOrderBuy")
    private Integer idOrderBuy;
    @Column("idProduct")
    private Integer idProduct;
    @Column("quantity")
    private int quantity;
    @Column("totalprice")
    private double totalprice;
}
