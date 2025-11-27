package com.example.softwarebackend.modules.submission.repository;

import com.example.softwarebackend.modules.submission.model.GradedResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GradedResultRepository extends JpaRepository<GradedResult, UUID> {
}
