package com.perepalacin.cart_service.repository;

import com.perepalacin.cart_service.entity.dao.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
