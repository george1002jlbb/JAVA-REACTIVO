package com.carritocompras.repository;

import com.carritocompras.model.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends R2dbcRepository<Product,Integer> {
}
