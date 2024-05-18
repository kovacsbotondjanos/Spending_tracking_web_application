package com.example.monthlySpendingsBackend.dataBaseHandler.models.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);
    Optional<CustomUser> findByEmail(String email);
    Optional<CustomUser> findById(Long id);
}