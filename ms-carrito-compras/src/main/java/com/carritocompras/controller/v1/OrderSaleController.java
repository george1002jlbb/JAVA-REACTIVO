package com.carritocompras.controller.v1;

import com.carritocompras.model.OrderBuy;
import com.carritocompras.model.OrderSale;
import com.carritocompras.service.OrderSaleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ordersales")
@AllArgsConstructor
public class OrderSaleController {
    private final OrderSaleService orderSaleService;

    @GetMapping
    public Flux<OrderSale> getAllOrderSales() {
        return orderSaleService.getAllSalesOrders();
    }

    @GetMapping("/{id}")
    public Mono<OrderSale> getOrderSaleById(@PathVariable Integer id) {
        return orderSaleService.getSalesOrderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OrderSale> createOrderSale(@RequestBody OrderSale orderSale) {
        return orderSaleService.createSalesOrder(orderSale);
    }

    @PutMapping("/{id}")
    public Mono<OrderSale> updateOrderSale(@PathVariable Integer id, @RequestBody OrderSale orderSale) {
        return orderSaleService.updateSalesOrder(id, orderSale);
    }

    @PutMapping("/{idOrderSale}/confirm")
    public Mono<ResponseEntity<OrderSale>> updateOrderStatusToConfirmed(
            @PathVariable Integer idOrderSale,
            @RequestBody OrderSale orderSale) {
        return orderSaleService.getSalesOrderById(idOrderSale) // Buscar la orden por ID
                .flatMap(existingOrder ->
                        orderSaleService.updateStateSalesOrderCONFIRMED(idOrderSale, orderSale)
                                .map(updatedOrder -> ResponseEntity.ok(updatedOrder)) // Devolver 200 OK con la orden actualizada
                                .onErrorResume(e -> {
                                    // Log the error for debugging purposes
                                    System.err.println("Error occurred: " + e.getMessage());
                                    // Return an empty Mono with a specific error if needed
                                    return Mono.error(new RuntimeException("Internal server error")); // Customize the error as needed
                                })
                )
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())); // Manejar caso cuando la orden no existe
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteOrderSale(@PathVariable Integer id) {
        return orderSaleService.deleteSalesOrder(id);
    }
}
