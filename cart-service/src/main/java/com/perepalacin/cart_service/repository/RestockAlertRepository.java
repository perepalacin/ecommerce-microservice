package com.perepalacin.cart_service.repository;

import com.perepalacin.cart_service.entity.dao.RestockAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestockAlertRepository extends JpaRepository<RestockAlert, Long> {
    List<RestockAlert> findByProductId(Long productId);
}
