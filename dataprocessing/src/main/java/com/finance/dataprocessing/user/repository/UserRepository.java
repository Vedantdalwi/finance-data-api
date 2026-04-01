package com.finance.dataprocessing.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.dataprocessing.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
