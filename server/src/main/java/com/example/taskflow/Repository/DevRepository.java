package com.example.taskflow.Repository;

import com.example.taskflow.Model.Dev;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DevRepository extends JpaRepository<Dev, UUID> {
    Optional<Dev> findByKeycloakId(String keycloakId);

    Optional<Dev> findByEmail(String email);
}