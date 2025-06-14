package com.perepalacin.cart_service.service;

import com.perepalacin.cart_service.client.ProductServiceClient;
import com.perepalacin.cart_service.entity.dao.Cart;
import com.perepalacin.cart_service.entity.dao.CartItem;
import com.perepalacin.cart_service.entity.dto.CartDto;
import com.perepalacin.cart_service.entity.dto.CartItemDto;
import com.perepalacin.cart_service.entity.dto.ProductDto;
import com.perepalacin.cart_service.entity.dto.UserDetailsDto;
import com.perepalacin.cart_service.exceptions.ProductNotFoundException;
import com.perepalacin.cart_service.exceptions.UnauthorizedException;
import com.perepalacin.cart_service.repository.CartItemRepository;
import com.perepalacin.cart_service.repository.CartRepository;
import com.perepalacin.cart_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;
    private final CartItemRepository cartItemRepository;

    public CartDto getUserCart () {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }
        Cart cart = cartRepository.getByUserId(userDetails.getUserId()).orElse(null);

        if (cart != null) {
            List<CartItemDto> cartItems = this.getCartItemDetails(cart);
            return CartDto.builder().id(cart.getId()).items(cartItems).build();
        } else {
            return CartDto.builder().build();
        }
    }

    public CartDto getCartByUserId (final UUID userId) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null || !userDetails.getUserId().equals(userId)) {
            throw new UnauthorizedException();
        }
        Cart cart = cartRepository.getByUserId(userId).orElse(null);

        if (cart != null) {
            List<CartItemDto> cartItems = this.getCartItemDetails(cart);
            return CartDto.builder().id(cart.getId()).items(cartItems).build();
        } else {
            return CartDto.builder().build();
        }
    }


    @Transactional
    public void addItemToCart (final Long productId, final Integer quantity) {
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
                return;
            }
        }

        ResponseEntity<ProductDto> productResponse = productServiceClient.getProductById(productId);
        if (productResponse.getStatusCode().is2xxSuccessful()) {
            cart.getItems().add(CartItem.builder().productId(productId).quantity(quantity).cart(cart).build());
            cartRepository.save(cart);
            return;
        } else if (HttpStatus.NOT_FOUND.equals(productResponse.getStatusCode())) {
            throw new ProductNotFoundException("Product with id: " + productId + " not found");
        } else {
            throw new RuntimeException("Something went wrong, please try again later");
        }
    }

    public CartItemDto editCartItemQuantity (final Long cartItemId, final Integer quantity) {
        UserDetailsDto userDetails = JwtUtil.getCredentialsFromToken();
        if (userDetails.getUserId() == null) {
            throw new UnauthorizedException();
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () ->  new ProductNotFoundException("Cart item with id: " + cartItemId + " not found")
        );

        ResponseEntity<ProductDto> productResponse = productServiceClient.getProductById(cartItem.getProductId());

        if (!productResponse.getStatusCode().is2xxSuccessful() || productResponse.getBody() == null) {
            throw new RuntimeException("Something went wrong while getting the product details, please try again later");
        }

        if (quantity == 0) {
            cartItemRepository.delete(cartItem);
            return null;
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return CartItemDto.builder()
                    .id(cartItem.getId())
                    .quantity(quantity)
                    .price(productResponse.getBody().getPrice())
                    .stock(productResponse.getBody().getStock())
                    .name(productResponse.getBody().getName())
                    .imageUrl(productResponse.getBody().getImageUrl())
                    .publicUrl(productResponse.getBody().getPublicUrl())
                    .productId(cartItem.getProductId())
                    .build();
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

    private List<CartItemDto> getCartItemDetails (Cart cart) {
        List<Long> productIds = new ArrayList<>();
        cart.getItems().forEach(item -> productIds.add(item.getProductId()));

        ResponseEntity<List<ProductDto>> productResponse = productServiceClient.getListOfProductsById(productIds);

        if (productResponse.getStatusCode().is2xxSuccessful() && productResponse.getBody() != null) {
            List<CartItemDto> cartItems = new ArrayList<>();
            productResponse.getBody().forEach(product -> {

                Optional<CartItem> foundItem = cart.getItems().stream()
                        .filter(item -> item.getProductId() != null && item.getProductId().equals(product.getId()))
                        .findFirst();

                if (foundItem.isEmpty()) {
                    throw new RuntimeException("There seems to be an error with the items of your cart, please try again later.");
                }

                cartItems.add(CartItemDto.builder()
                        .id(foundItem.get().getId())
                        .quantity(foundItem.get().getQuantity())
                        .productId(product.getId())
                        .name(product.getName())
                        .brand(product.getBrand())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .publicUrl(product.getPublicUrl())
                        .productId(product.getId())
                        .imageUrl(product.getImageUrl())
                        .build());
                return;
            });

            return cartItems;
        } else {
            throw new RuntimeException("Something went wrong while fetching the products in your cart, please try again later.");
        }
    }
}

