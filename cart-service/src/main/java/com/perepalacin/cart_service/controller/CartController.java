package com.perepalacin.cart_service.controller;

import com.perepalacin.cart_service.entity.dao.Cart;
import com.perepalacin.cart_service.entity.dao.CartItem;
import com.perepalacin.cart_service.entity.dto.CartDto;
import com.perepalacin.cart_service.entity.dto.CartItemDto;
import com.perepalacin.cart_service.entity.dto.ProductDto;
import com.perepalacin.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getUserCart () {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getUserCart());
    }

    @GetMapping("{userId}")
    public ResponseEntity<CartDto> getCartByUserId (@PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<CartDto> addItemToTheCart (@RequestParam (name = "productId") Long productId, @RequestParam (name="quantity", required = false) Integer quantity) {
        if (quantity == null) {
            quantity = 1;
        }
        cartService.addItemToCart(productId, quantity);
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getUserCart());
    }

    @PatchMapping("/quantity")
    public ResponseEntity<CartItemDto> changeItemQuantity (@RequestParam (name = "cartItemId") Long cartItemId, @RequestParam (name="quantity") Integer quantity) {
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
