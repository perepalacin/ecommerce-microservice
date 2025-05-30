package com.perepalacin.cart_service.controller;

import com.perepalacin.cart_service.entity.dao.Cart;
import com.perepalacin.cart_service.entity.dao.CartItem;
import com.perepalacin.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getUserCart () {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getUserCart());
    }

    @PostMapping
    public ResponseEntity<Cart> addItemToTheCart (@RequestParam (name = "productId") Long productId, @RequestParam (name="quantity") Integer quantity) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.addItemToCart(productId, quantity));
    }

    //This should be debounced on the FE
    @PatchMapping("/quantity")
    public ResponseEntity<CartItem> changeItemQuantity (@RequestParam (name = "cartItemId") Long cartItemId, @RequestParam (name="quantity") Integer quantity) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.editCartItemQuantity(cartItemId, quantity));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteCartItem (@RequestParam (name="cartItemId") Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> emptyCart () {
        cartService.emptyUserCart();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
