package com.carritocompras.repository;

import com.carritocompras.model.DetailCart;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IDetailCartRepository extends R2dbcRepository<DetailCart, Integer> {

    Flux<DetailCart> findByidShoppingCart(Integer idShoppingCart);

    Mono<DetailCart> findByidProductAndIdShoppingCart(Integer idProduct, Integer idShoppingCart);

    Mono<Void> deleteByidDetailCart(Integer idDetailCart);

    Mono<Void> deleteByidProductAndIdShoppingCart(Integer idProduct, Integer idShoppingCart);
    Mono<Void> deleteByIdShoppingCart(Integer idShoppingCart);

    Mono<DetailCart> findByIdDetailCartAndIdShoppingCart(Integer idDetailCart, Integer idShoppingCart);
}

