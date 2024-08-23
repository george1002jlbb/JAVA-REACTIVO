package com.carritocompras.controller.v1;

import com.carritocompras.model.ShoppingCart;
import com.carritocompras.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/shoppingcarts")
@AllArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public Flux<ShoppingCart> getAllShoppingCarts() {
        return shoppingCartService.getAllShoppingCartsWithProducts();
    }

    @GetMapping("/{cartId}")
    public Mono<ShoppingCart> getAllShoppingCart(@PathVariable Integer cartId) {
        return shoppingCartService.getAllShoppingCartWithProducts(cartId);
    }

    /*@GetMapping("/{cartId}")
    public Mono<ShoppingCart> getShoppingCartById(@PathVariable Integer cartId) {
        return shoppingCartService.getShoppingCartById(cartId);
    }*/

    @PostMapping
    public Mono<ShoppingCart> createShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.createShoppingCart(shoppingCart.getIdcustomer());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteShoppingCart(@PathVariable Integer id) {
        return shoppingCartService.deleteShoppingCart(id);
    }

    @PostMapping("/{cartId}/add-product")
    public Mono<Void> addProductToCart(@PathVariable Integer cartId, @RequestParam Integer productId, @RequestParam int quantity) {
        return shoppingCartService.addProductToCart(cartId, productId, quantity);
    }

    @DeleteMapping("/{cartId}/remove-product/{productId}")
    public Mono<Void> deleteProductToCart(@PathVariable Integer cartId, @PathVariable Integer productId) {
        return shoppingCartService.deleteProductToCart(cartId, productId);
    }

    @DeleteMapping("/empty-the-cart/{cartId}")
    public Mono<Void> emptyTheCart(@PathVariable Integer cartId) {
        return shoppingCartService.emptyTheCart(cartId);
    }

    @GetMapping("/{idShoppingCart}/total")
    public Mono<Void> calculateTotal(@PathVariable Integer idShoppingCart) {
        return shoppingCartService.totalPurchase(idShoppingCart);
    }

}
