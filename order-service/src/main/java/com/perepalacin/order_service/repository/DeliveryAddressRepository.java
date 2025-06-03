package com.perepalacin.order_service.repository;

import com.perepalacin.order_service.entity.dao.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
}
