package com.carritocompras.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportTopProducDto {
    private Integer idOrderSale;
    private Integer idCustomer;
    private Integer idShoppingCart;
    private Integer idProduct;
    private String description;
    private int quantity;
}
