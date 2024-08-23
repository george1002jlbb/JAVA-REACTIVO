package com.carritocompras.service;

import com.carritocompras.enums.OrderStatus;
import com.carritocompras.exception.NotFoundException;
import com.carritocompras.model.Customer;
import com.carritocompras.model.OrderBuy;
import com.carritocompras.model.OrderSale;
import com.carritocompras.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrderSaleService {
    private final IOrderSaleRepository iOrderSaleRepository;
    private final ICustomerRepository iCustomerRepository;
    private final IShoppingCartRepository iShoppingCartRepository;
    private final IDetailCartRepository iDetailCartRepository;
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;

    public Flux<OrderSale> getAllSalesOrders() {
        return iOrderSaleRepository.findAll();
    }

    public Mono<OrderSale> getSalesOrderById(Integer idOrderSale) {
        return iOrderSaleRepository.findById(idOrderSale);
    }

    public Mono<OrderSale> createSalesOrder(OrderSale orderSale) {
        // Verificar la existencia del carrito de compras
        return iShoppingCartRepository.findById(orderSale.getIdShoppingCart())
                .switchIfEmpty(Mono.error(new NotFoundException("Cart not found with id: " + orderSale.getIdShoppingCart())))
                .flatMap(shoppingCart -> {
                    orderSale.setTotalBuy( shoppingCart.getTotalBuy()+shoppingCart.getShippingCost()+shoppingCart.getTax() );
                    orderSale.setOrderDate(LocalDateTime.now());
                    orderSale.setOrderStatus(OrderStatus.PENDING);
                    return iOrderSaleRepository.save(orderSale);
                });
    }

    public Mono<OrderSale> updateStateSalesOrderCONFIRMED(Integer idOrderSale, OrderSale orderSale) {
        return iOrderSaleRepository.findById(idOrderSale)
                .flatMap(existingOrder -> {
                    // Verificar si la orden ya está confirmada
                    if (existingOrder.getOrderStatus() == OrderStatus.CONFIRMED) {
                        return Mono.error(new IllegalStateException("Order is already confirmed"));
                    }

                    // Si la orden no está confirmada, proceder con la actualización
                    return shoppingCartService.getAllShoppingCartWithProducts(existingOrder.getIdShoppingCart()) // Recuperar el carrito de compras con productos
                            .flatMap(shoppingCart ->
                                    Flux.fromIterable(shoppingCart.getProducts()) // Actualizar el inventario para cada producto en el carrito
                                            .flatMap(produDetailCart ->
                                                    productService.getProductById(produDetailCart.getIdProduct())
                                                            .flatMap(product -> {
                                                                // Calcular la nueva cantidad en inventario
                                                                int updatedQuantity = product.getStock() - produDetailCart.getQuantity();
                                                                // Verificar si la cantidad es válida
                                                                if (updatedQuantity < 0) {
                                                                    return Mono.error(new RuntimeException("Insufficient stock for product " + product.getIdProduct()));
                                                                }
                                                                // Actualizar el stock del producto
                                                                return productService.updateStock(product.getIdProduct(), updatedQuantity)
                                                                        .thenReturn(product);
                                                            })
                                            )
                                            .then(Mono.defer(() -> {
                                                // Actualizar el estado de la orden
                                                existingOrder.setOrderStatus(OrderStatus.CONFIRMED);
                                                return iOrderSaleRepository.save(existingOrder);
                                            }))
                            );
                })
                .onErrorResume(e -> {
                    // Log the error for debugging purposes
                    System.err.println("Error occurred: " + e.getMessage());
                    // Return an empty Mono with a specific error if needed
                    return Mono.error(new RuntimeException("Internal server error")); // Customize the error as needed
                });
    }



    public Mono<OrderSale> updateStateSalesOrderSHIPPED(Integer idOrderSale, OrderSale orderSale) {
        return iOrderSaleRepository.findById(idOrderSale)
                .flatMap(existingOrder -> {
                    existingOrder.setOrderStatus(OrderStatus.SHIPPED);
                    return iOrderSaleRepository.save(existingOrder);
                });
    }

    public Mono<OrderSale> updateStateSalesOrderDELIVERED(Integer idOrderSale, OrderSale orderSale) {
        return iOrderSaleRepository.findById(idOrderSale)
                .flatMap(existingOrder -> {
                    existingOrder.setOrderStatus(OrderStatus.DELIVERED);
                    return iOrderSaleRepository.save(existingOrder);
                });
    }

    public Mono<OrderSale> updateStateSalesOrderCANCELLED(Integer idOrderSale, OrderSale orderSale) {
        return iOrderSaleRepository.findById(idOrderSale)
                .flatMap(existingOrder -> {
                    existingOrder.setOrderStatus(OrderStatus.CANCELLED);
                    return iOrderSaleRepository.save(existingOrder);
                });
    }

    public Mono<OrderSale> updateSalesOrder(Integer idOrderSale, OrderSale orderSale) {
        return iOrderSaleRepository.findById(idOrderSale)
                .flatMap(existingOrder -> {
                    return iOrderSaleRepository.save(existingOrder);
                });
    }

    public Mono<Void> deleteSalesOrder(Integer idOrderSale) {
        return iOrderSaleRepository.deleteById(idOrderSale);
    }

    public Mono<Customer> getCustomerById(Integer idCustomer) {
        return iCustomerRepository.findById(idCustomer);
    }
}
