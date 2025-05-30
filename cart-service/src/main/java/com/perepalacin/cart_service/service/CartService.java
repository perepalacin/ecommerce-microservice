package com.perepalacin.cart_service.service;

import com.perepalacin.cart_service.client.ProductServiceClient;
import com.perepalacin.cart_service.entity.dao.Cart;
import com.perepalacin.cart_service.entity.dao.CartItem;
import com.perepalacin.cart_service.entity.dao.ProductDao;
import com.perepalacin.cart_service.entity.dto.UserDetailsDto;
import com.perepalacin.cart_service.exceptions.ProductNotFoundException;
import com.perepalacin.cart_service.exceptions.UnauthorizedException;
import com.perepalacin.cart_service.repository.CartItemRepository;
import com.perepalacin.cart_service.repository.CartRepository;
import com.perepalacin.cart_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;
    private final CartItemRepository cartItemRepository;

    public Cart getUserCart () {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        return cartRepository.getByUserId(userDetails.getUserId()).orElse(null);
    }

    @Transactional
    public Cart addItemToCart (final Long productId, final Integer quantity) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        Cart cart;

        Optional<Cart> optionalCart = cartRepository.getByUserId(userDetails.getUserId());
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            cart = Cart.builder()
                    .userId(userDetails.getUserId())
                    .build();
            cart = cartRepository.saveAndFlush(cart);
        }

        for (int i = 0; i < cart.getItems().size(); i++) {
            if (Objects.equals(cart.getItems().get(i).getProductId(), productId)) {
                cart.getItems().get(i).setQuantity(cart.getItems().get(i).getQuantity() + quantity);
                cartRepository.save(cart);
                return cart;
            }
        }

        ResponseEntity<ProductDao> productResponse = productServiceClient.getProductById(productId);
        if (HttpStatus.OK.equals(productResponse.getStatusCode())) {
            cart.getItems().add(CartItem.builder().productId(productId).quantity(quantity).cart(cart).build());
            cartRepository.save(cart);
            return cart;
        } else if (HttpStatus.NOT_FOUND.equals(productResponse.getStatusCode())) {
            throw new ProductNotFoundException("Product with id: " + productId + " not found");
        } else {
            throw new RuntimeException("Something went wrong, please try again later");
        }
    }

    public CartItem editCartItemQuantity (final Long cartItemId, final Integer quantity) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () ->  new ProductNotFoundException("Cart item with id: " + cartItemId + " not found")
        );

        if (quantity == 0) {
            cartItemRepository.delete(cartItem);
            return null;
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return cartItem;
        }
    }

    public void deleteCartItem (final Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new ProductNotFoundException("Cart item with id: " + cartItemId + " not found")
        );

        cartItemRepository.delete(cartItem);
    }

    public void emptyUserCart () {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails == null || userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        Cart cart = cartRepository.getByUserId(userDetails.getUserId()).orElseThrow(
                UnauthorizedException::new
        );

        cartRepository.delete(cart);
    }
}
