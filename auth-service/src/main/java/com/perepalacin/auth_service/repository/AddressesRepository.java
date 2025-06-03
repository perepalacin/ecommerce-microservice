package com.perepalacin.auth_service.repository;

import com.perepalacin.auth_service.entity.dao.AddressDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressesRepository extends JpaRepository<AddressDao, Long> {
    List<AddressDao> findByUserId_Id(UUID userId);
    Optional<AddressDao> findByIdAndUserId_Id(long id, UUID userId);
    Optional<AddressDao> findByUserId_IdAndDefaultAddressTrue(UUID userId);
    List<AddressDao> findByIdIn(List<Long> addressesIds);
}
