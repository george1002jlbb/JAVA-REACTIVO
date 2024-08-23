package com.carritocompras.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("SHOPPINGCART")
public class ShoppingCart {
    @Id
    @Column("idshoppingcart")
    private Integer idShoppingCart;
    @Column("idcustomer")
    private Integer idcustomer;
    @Transient
    private List<DetailCart> products = new ArrayList<>();
    @Column("creationdate")
    private LocalDateTime creationdate;
    @Column("totalbuy")
    private double totalBuy;
    @Column("shippingcost")
    private double shippingCost;
    @Column("tax")
    private double tax;
}
