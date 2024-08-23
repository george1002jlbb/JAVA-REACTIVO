package com.carritocompras.service;

import com.carritocompras.enums.OrderStatus;
import com.carritocompras.exception.InsufficientStockException;
import com.carritocompras.exception.NotFoundException;
import com.carritocompras.model.Customer;
import com.carritocompras.model.ItemOrder;
import com.carritocompras.model.OrderBuy;
import com.carritocompras.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrderBuyService {
    private final IOrderBuyRepository iOrderBuyRepository;
    private final ICustomerRepository icustomerRepository;
    private final ISupplierRepository iSupplierRepository;
    private final IItemOrderRepository itemOrderRepository;
    private final IProductRepository iProductRepository;
    private final ProductService productService;

    public Flux<OrderBuy> getAllOrderBuys() {
        return iOrderBuyRepository.findAll();
    }

    public Mono<OrderBuy> getOrderBuyById(Integer idOrderBuy) {
        return iOrderBuyRepository.findById(idOrderBuy);
    }

    public Mono<OrderBuy> createPurchaseOrder(OrderBuy orderBuy) {
        return iSupplierRepository.findById(orderBuy.getIdSupplier())
                .flatMap(existingSupplier -> {
                    if (existingSupplier != null) {
                        orderBuy.setOrderDate(LocalDateTime.now());
                        return iOrderBuyRepository.save(orderBuy);
                    }else {
                        return Mono.error(new NotFoundException("Supplier not found with id: " + orderBuy.getIdSupplier()));
                    }
                });
    }

    public Flux<OrderBuy> getAllOrderBuyWithProducts() {
        // Obtener todas las órdenes de compra
        return iOrderBuyRepository.findAll()
                // Para cada orden de compra, buscar los productos asociados
                .flatMap(orderBuy ->
                        // Buscar los productos asociados con la orden de compra
                        itemOrderRepository.findByidOrderBuy(orderBuy.getIdOrderBuy())
                                .collectList()
                                .map(itemOrders -> {
                                    // Aquí puedes llenar la lista de productos en la orden de compra
                                    orderBuy.setProducts(itemOrders); // Asume que `setProducts` existe
                                    return orderBuy;
                                })
                );
    }

    public Mono<OrderBuy> getAllOrderBuyWithProducts(Integer idOrderBuy) {
        // Obtener todas las órdenes de compra
        return iOrderBuyRepository.findById(idOrderBuy)
                // Para cada orden de compra, buscar los productos asociados
                .flatMap(orderBuy ->
                        // Buscar los productos asociados con la orden de compra
                        itemOrderRepository.findByidOrderBuy(orderBuy.getIdOrderBuy())
                                .collectList()
                                .map(itemOrders -> {
                                    // Aquí puedes llenar la lista de productos en la orden de compra
                                    orderBuy.setProducts(itemOrders); // Asume que `setProducts` existe
                                    return orderBuy;
                                })
                );
    }


    public Mono<OrderBuy> updateOrderBuyStatusToConfirmed(Integer idOrderBuy, OrderBuy orderBuy) {
        return iOrderBuyRepository.findById(idOrderBuy)
                .flatMap(existingOrder -> {
                    // Verificar si la orden ya está confirmada
                    if (existingOrder.getOrderStatus() == OrderStatus.CONFIRMED) {
                        return Mono.error(new IllegalStateException("Order is already confirmed"));
                    }

                    // Si la orden no está confirmada, proceder con la actualización
                    return getAllOrderBuyWithProducts(existingOrder.getIdOrderBuy()) // Recuperar el carrito de compras con productos
                            .flatMap(orderbuy ->
                                    Flux.fromIterable(orderbuy.getProducts()) // Actualizar el inventario para cada producto en el carrito
                                            .flatMap(itemOrder ->
                                                    productService.getProductById(itemOrder.getIdProduct())
                                                            .flatMap(product -> {
                                                                // Calcular la nueva cantidad en inventario
                                                                int updatedQuantity = product.getStock() + itemOrder.getQuantity();
                                                                // Actualizar el stock del producto
                                                                return productService.updateStock(product.getIdProduct(), updatedQuantity)
                                                                        .thenReturn(product);
                                                            })
                                            )
                                            .then(Mono.defer(() -> {
                                                // Actualizar el estado de la orden
                                                existingOrder.setOrderStatus(OrderStatus.CONFIRMED);
                                                return iOrderBuyRepository.save(existingOrder);
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


    public Mono<Void> addProductToOrder(Integer idOrderBuy, Integer idProduct, int quantity) {
        return iOrderBuyRepository.findById(idOrderBuy)
                .switchIfEmpty(Mono.error(new NotFoundException("Order not found with id: " + idOrderBuy)))
                .flatMap(orderBuy ->
                        iProductRepository.findById(idProduct)
                                .switchIfEmpty(Mono.error(new NotFoundException("Product not found with id: " + idProduct)))
                                .flatMap(product -> {
                                    if (product.getStock() < quantity) {
                                        return Mono.error(new InsufficientStockException("Insufficient stock for product id: " + idProduct));
                                    }
                                    return itemOrderRepository.findByidProductAndIdOrderBuy(idProduct, idOrderBuy)
                                            .flatMap(existingItemOrder -> {
                                                existingItemOrder.setQuantity(existingItemOrder.getQuantity() + quantity);
                                                existingItemOrder.setTotalprice(existingItemOrder.getQuantity() * product.getPrice());
                                                return itemOrderRepository.save(existingItemOrder);
                                            })
                                            .switchIfEmpty(
                                                    Mono.defer(() -> {
                                                        ItemOrder newItemOrder = new ItemOrder();
                                                        newItemOrder.setIdProduct(idProduct);
                                                        newItemOrder.setIdOrderBuy(idOrderBuy);
                                                        newItemOrder.setQuantity(quantity);
                                                        newItemOrder.setTotalprice(product.getPrice() * quantity);
                                                        return itemOrderRepository.save(newItemOrder);
                                                    })
                                            )
                                            .doOnError(error -> {
                                                System.err.println("Error in addProductToCart: " + error.getMessage());
                                            });
                                })
                ).then();
    }


    public Mono<Void> deleteProductToOrder(Integer IdOrderBuy, Integer idProduct) {
        return itemOrderRepository.deleteByidProductAndIdOrderBuy(idProduct, IdOrderBuy);
    }

    public Mono<Void> deletePurchaseOrder(Integer idOrderBuy) {
        return iOrderBuyRepository.deleteById(idOrderBuy);
    }

    public Mono<Customer> getCustomerById(Integer idCustomer) {
        return icustomerRepository.findById(idCustomer);
    }
}
