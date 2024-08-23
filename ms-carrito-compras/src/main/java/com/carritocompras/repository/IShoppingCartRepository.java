package com.carritocompras.repository;

import com.carritocompras.model.ShoppingCart;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShoppingCartRepository extends R2dbcRepository<ShoppingCart,Integer> {
}
