package com.perepalacin.cart_service.repository;

import com.perepalacin.cart_service.entity.dao.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> getByUserId(UUID userId);
}
