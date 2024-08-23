package com.carritocompras.model;

import com.carritocompras.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("ORDERSALE")
public class OrderSale { // ORDEN DE VENTA
    @Id
    @Column("idordersale")
    private Integer idOrderSale;
    @Column("idcustomer")
    private Integer idcustomer;
    @Column("idshoppingcart")
    private Integer idShoppingCart;
    @Column("orderdate")
    private LocalDateTime orderDate;
    @Column("totalbuy")
    private double totalBuy;
    @Column("orderstatus")
    private OrderStatus orderStatus; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
}
