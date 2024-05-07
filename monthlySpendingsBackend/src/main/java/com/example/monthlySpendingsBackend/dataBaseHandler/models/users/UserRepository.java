package com.example.monthlySpendingsBackend.dataBaseHandler.models.users;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);
    CustomUser getById(Id id);
    Optional<CustomUser> findByEmail(String email);
}