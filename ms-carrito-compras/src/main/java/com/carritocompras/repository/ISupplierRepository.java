package com.carritocompras.repository;

import com.carritocompras.model.Supplier;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISupplierRepository extends R2dbcRepository<Supplier,Integer> {
}
