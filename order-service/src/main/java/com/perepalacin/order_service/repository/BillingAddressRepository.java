package com.perepalacin.order_service.repository;

import com.perepalacin.order_service.entity.dao.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {
}
