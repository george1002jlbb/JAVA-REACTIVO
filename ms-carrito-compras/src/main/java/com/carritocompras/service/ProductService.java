package com.carritocompras.service;

import com.carritocompras.exception.NotFoundException;
import com.carritocompras.model.Product;
import com.carritocompras.repository.IProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ProductService {
    private final IProductRepository iProductRepository;

    public Flux<Product> getAllProducts() {
        return iProductRepository.findAll();
    }

    public Mono<Product> getProductById(Integer idProduct) {
        return iProductRepository.findById(idProduct);
    }

    public Mono<Product> createProduct(Product product) {
        return iProductRepository.save(product);
    }

    public Mono<Product> updateProduct(Integer idProduct, Product product) {
        return iProductRepository.findById(idProduct)
                .flatMap(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setDepartment(product.getDepartment());
                    existingProduct.setMaterial(product.getMaterial());
                    existingProduct.setImageUrl(product.getImageUrl());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setStock(product.getStock());
                    return iProductRepository.save(existingProduct);
                });
    }

    public Mono<Void> deleteProduct(Integer idProduct) {
        return iProductRepository.deleteById(idProduct);
    }

    public Mono<Product> updateStock(Integer idProducto, int newStock) {
        return iProductRepository.findById(idProducto)
                .switchIfEmpty(Mono.error(new NotFoundException("product not found with id: " + idProducto)))
                .flatMap(producto -> {
                    producto.setStock(newStock);
                    return iProductRepository.save(producto);
                });
    }

}
