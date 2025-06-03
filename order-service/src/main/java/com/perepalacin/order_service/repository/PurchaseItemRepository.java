package com.perepalacin.order_service.repository;

import com.perepalacin.order_service.entity.dao.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}
