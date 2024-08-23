package com.carritocompras.repository;

import com.carritocompras.model.OrderBuy;
import com.carritocompras.model.OrderSale;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface IOrderSaleRepository extends R2dbcRepository<OrderSale,Integer> {
    Flux<OrderSale> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
