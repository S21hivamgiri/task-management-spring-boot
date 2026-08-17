package com.example.taskflow.Repository;

import com.example.taskflow.Model.Dev;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DevRepository extends JpaRepository<Dev, UUID> {
}
