package com.perepalacin.auth_service.repository;

import com.perepalacin.auth_service.entity.dao.AddressDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressesRepository extends JpaRepository<AddressDao, Long> {
}
