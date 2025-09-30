package com.example.softwarebackend.repository;

import com.example.softwarebackend.model.GradedResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GradedResultRepository extends JpaRepository<GradedResult, UUID> {
}
