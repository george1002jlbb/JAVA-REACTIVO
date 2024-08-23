package com.carritocompras.model;

import com.carritocompras.enums.OrderStatus;
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
@Table("ORDERBUY")
public class OrderBuy { // ORDEN DE COMPRA
    @Id
    @Column("idorderbuy")
    private Integer idOrderBuy;
    @Column("idsupplier")
    private Integer idSupplier;
    @Transient
    private List<ItemOrder> products = new ArrayList<>();
    @Column("orderdate")
    private LocalDateTime orderDate;
    @Column("orderstatus")
    private OrderStatus orderStatus; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
}
