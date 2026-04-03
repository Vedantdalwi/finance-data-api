package com.finance.dataprocessing.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.dataprocessing.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	
    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

}
