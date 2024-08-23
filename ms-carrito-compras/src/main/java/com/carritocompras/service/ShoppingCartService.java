package com.carritocompras.service;

import com.carritocompras.exception.InsufficientStockException;
import com.carritocompras.exception.NotFoundException;
import com.carritocompras.model.*;
import com.carritocompras.repository.ICustomerRepository;
import com.carritocompras.repository.IDetailCartRepository;
import com.carritocompras.repository.IProductRepository;
import com.carritocompras.repository.IShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ShoppingCartService {
    private final IShoppingCartRepository iShoppingCartRepository;
    private final IDetailCartRepository iDetailCartRepository;
    private final IProductRepository iProductRepository;
    private final ICustomerRepository iCustomerRepository;
    private final ProductService productService;
    private static final double TAX_RATE = 0.19; // 10% de impuesto
    private static final double SHIPMENT_RATE = 0.20; // 10% de costo de envio

    public Flux<ShoppingCart> getAllShoppingCarts() {
        return iShoppingCartRepository.findAll();
    }

    public Mono<ShoppingCart> getShoppingCartById(Integer id) {
        return iShoppingCartRepository.findById(id);
    }

    public Mono<ShoppingCart> createShoppingCart(Integer idCustomer) {
        return iCustomerRepository.findById(idCustomer)
                .flatMap(customer -> {
                    ShoppingCart shoppingCart = ShoppingCart.builder()
                            .idcustomer(customer.getIdCustomer())
                            .creationdate(LocalDateTime.now())
                            .build();
                    return iShoppingCartRepository.save(shoppingCart);
                });
    }

    public Mono<Void> deleteShoppingCart(Integer idShoppingCart) {
        return iShoppingCartRepository.deleteById(idShoppingCart);
    }

    public Flux<ShoppingCart> getAllShoppingCartsWithProducts() {
        Flux<ShoppingCart> shoppingCartFlux = iShoppingCartRepository.findAll();

        return shoppingCartFlux.flatMap(shoppingCart -> {
            return iDetailCartRepository.findByidShoppingCart(shoppingCart.getIdShoppingCart())
                    .collectList().doOnNext(shoppingCart::setProducts)
                    .map(detailCarts -> shoppingCart);
        });
    }

    public Mono<ShoppingCart> getAllShoppingCartWithProducts(Integer idShoppingCart) {
        // Obtiene el carrito de compras
        return iShoppingCartRepository.findById(idShoppingCart)
                .flatMap(shoppingCart -> {
                    return iDetailCartRepository.findByidShoppingCart(idShoppingCart)
                            .collectList().doOnNext(shoppingCart::setProducts)
                            .map(detailCarts -> shoppingCart);
                });
    }

    public Mono<Void> addProductToCart(Integer idShoppingCart, Integer idProduct, int quantity) {
        return iShoppingCartRepository.findById(idShoppingCart)
                .switchIfEmpty(Mono.error(new NotFoundException("Cart not found with id: " + idShoppingCart)))
                .flatMap(cart ->
                        iProductRepository.findById(idProduct)
                                .switchIfEmpty(Mono.error(new NotFoundException("Product not found with id: " + idProduct)))
                                .flatMap(product -> {
                                    if (product.getStock() < quantity) {
                                        return Mono.error(new InsufficientStockException("Insufficient stock for product id: " + idProduct));
                                    }
                                    return iDetailCartRepository.findByidProductAndIdShoppingCart(idProduct, idShoppingCart)
                                            .flatMap(existingDetailCart -> {
                                                existingDetailCart.setQuantity(existingDetailCart.getQuantity() + quantity);
                                                existingDetailCart.setTotalprice(existingDetailCart.getQuantity() * product.getPrice());
                                                return iDetailCartRepository.save(existingDetailCart);
                                            })
                                            .switchIfEmpty(
                                                    Mono.defer(() -> {
                                                        DetailCart newDetailCart = new DetailCart();
                                                        newDetailCart.setIdProduct(idProduct);
                                                        newDetailCart.setIdShoppingCart(idShoppingCart);
                                                        newDetailCart.setQuantity(quantity);
                                                        newDetailCart.setTotalprice(product.getPrice() * quantity);
                                                        return iDetailCartRepository.save(newDetailCart);
                                                    })
                                            )
                                            .doOnError(error -> {
                                                System.err.println("Error in addProductToCart: " + error.getMessage());
                                            });
                                })
                ).then();
    }



    public Mono<Void> deleteProductToCart(Integer idShoppingCart, Integer idProduct) {
        return iDetailCartRepository.deleteByidProductAndIdShoppingCart(idProduct, idShoppingCart);
    }

    public Mono<Void> emptyTheCart(Integer idShoppingCart) {
        return iDetailCartRepository.deleteByIdShoppingCart(idShoppingCart);
    }

    public Mono<Void> updateStockProductInTheCart(Integer idShoppingCart, Integer idProduct, int newQuantity) {
        return iDetailCartRepository.findByidProductAndIdShoppingCart(idProduct, idShoppingCart)
                .flatMap(detailCart -> {
                    detailCart.setQuantity(newQuantity);
                    return iDetailCartRepository.save(detailCart).then();
                });
    }

    public Mono<Void> totalPurchase(Integer idShoppingCart) {
        return iShoppingCartRepository.findById(idShoppingCart)
                .switchIfEmpty(Mono.error(new NotFoundException("Cart not found with id: " + idShoppingCart)))
                .flatMap(shoppingCart -> calculateTotal(shoppingCart.getIdShoppingCart())
                        .doOnNext(total -> {
                            // Actualiza el carrito con el total, impuestos y costo de envío
                            shoppingCart.setTotalBuy(total);
                            shoppingCart.setTax(calculateTax(shoppingCart.getTotalBuy()));
                            shoppingCart.setShippingCost(calculateShipment(shoppingCart.getTotalBuy()));
                        })
                        .then(iShoppingCartRepository.save(shoppingCart))
                )
                .then();
    }

    public Mono<Double> calculateTotal(Integer idCart) {
        return getAllShoppingCartWithProducts(idCart)  // Obtener el carrito de compras
                .map(shoppingCart -> {
                    // Sumar el precio total de todos los productos en el carrito
                    return shoppingCart.getProducts().stream()
                            .mapToDouble(DetailCart::getTotalprice)  // Suponiendo que getTotalprice devuelve el precio total del producto
                            .sum();
                });
    }

    public double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }

    public double calculateShipment(double subtotal) {
        return subtotal * SHIPMENT_RATE;
    }
}
