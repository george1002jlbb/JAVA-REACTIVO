package com.carritocompras.repository;

import com.carritocompras.model.OrderBuy;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface IOrderBuyRepository extends R2dbcRepository<OrderBuy,Integer> {
    Flux<OrderBuy> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
