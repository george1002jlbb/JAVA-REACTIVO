package com.carritocompras.controller.v1;

import com.carritocompras.model.Customer;
import com.carritocompras.model.OrderBuy;
import com.carritocompras.model.OrderSale;
import com.carritocompras.service.OrderBuyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orderbuys")
@AllArgsConstructor
public class OrderBuyController {
    private final OrderBuyService orderBuyService;

    @GetMapping
    public Flux<OrderBuy> getAllOrderBuys() {
        return orderBuyService.getAllOrderBuyWithProducts();
    }

    @GetMapping("/{id}")
    public Mono<OrderBuy> getOrderBuyById(@PathVariable Integer id) {
        return orderBuyService.getOrderBuyById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<OrderBuy> createOrderBuy(@RequestBody OrderBuy orderBuy) {
        return orderBuyService.createPurchaseOrder(orderBuy);
    }

    @PostMapping("/{idOrderBuy}/add-product")
    public Mono<Void> addProductToOrder(@PathVariable Integer idOrderBuy, @RequestParam Integer productId, @RequestParam int quantity) {
        return orderBuyService.addProductToOrder(idOrderBuy, productId, quantity);
    }

    @DeleteMapping("/{idOrderBuy}/remove-product/{productId}")
    public Mono<Void> deleteProductToOrder(@PathVariable Integer idOrderBuy, @PathVariable Integer productId) {
        return orderBuyService.deleteProductToOrder(idOrderBuy, productId);
    }

    @PutMapping("/{idOrderBuy}/confirm")
    public Mono<ResponseEntity<OrderBuy>> updateOrderStatusToConfirmed(
            @PathVariable Integer idOrderBuy,
            @RequestBody OrderBuy orderBuy) {
        return orderBuyService.getOrderBuyById(idOrderBuy) // Buscar la orden por ID
                .flatMap(existingOrder ->
                        orderBuyService.updateOrderBuyStatusToConfirmed(idOrderBuy, orderBuy)
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
    public Mono<Void> deleteOrderBuy(@PathVariable Integer id) {
        return orderBuyService.deletePurchaseOrder(id);
    }
}
