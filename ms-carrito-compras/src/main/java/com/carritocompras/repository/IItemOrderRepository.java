package com.carritocompras.repository;

import com.carritocompras.model.DetailCart;
import com.carritocompras.model.ItemOrder;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IItemOrderRepository extends R2dbcRepository<ItemOrder, Integer> {
    Flux<ItemOrder> findByidOrderBuy(Integer idOrderBuy);
    Mono<ItemOrder> findByidProductAndIdOrderBuy(Integer idProduct, Integer idOrderBuy);

    Mono<Void> deleteByidProductAndIdOrderBuy(Integer idProduct, Integer IdOrderBuy);
    Mono<Void> deleteByIdOrderBuy(Integer IdOrderBuy);
}

