package com.carritocompras.service;

import com.carritocompras.model.Supplier;
import com.carritocompras.repository.ISupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class SupplierService {
    private final ISupplierRepository iSupplierRepository;

    public Flux<Supplier> getAllSuppliers() {
        return iSupplierRepository.findAll();
    }

    public Mono<Supplier> getSupplierById(Integer idSupplier) {
        return iSupplierRepository.findById(idSupplier);
    }

    public Mono<Supplier> createSupplier(Supplier supplier) {
        return iSupplierRepository.save(supplier);
    }

    public Mono<Supplier> updateSupplier(Integer idSupplier, Supplier supplier) {
        return iSupplierRepository.findById(idSupplier)
                .flatMap(existingSupplier -> {
                    existingSupplier.setName(supplier.getName());
                    existingSupplier.setJobType(supplier.getJobType());
                    existingSupplier.setCountry(supplier.getCountry());
                    existingSupplier.setCity(supplier.getCity());
                    existingSupplier.setAddress(supplier.getAddress());
                    return iSupplierRepository.save(existingSupplier);
                });
    }

    public Mono<Void> deleteSupplier(Integer idCustomer) {
        return iSupplierRepository.deleteById(idCustomer);
    }


}
