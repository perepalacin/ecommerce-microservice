package com.perepalacin.order_service.repository;

import com.perepalacin.order_service.entity.dao.Purchase;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByUserId(UUID userId);

}
