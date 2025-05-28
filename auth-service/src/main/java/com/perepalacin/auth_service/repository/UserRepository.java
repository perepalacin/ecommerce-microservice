package com.perepalacin.auth_service.repository;

import com.perepalacin.auth_service.entity.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserDao, UUID> {
}
