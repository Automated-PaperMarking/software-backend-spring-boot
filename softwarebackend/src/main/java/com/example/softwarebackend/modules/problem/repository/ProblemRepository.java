package com.example.softwarebackend.modules.problem.repository;

import com.example.softwarebackend.shared.entities.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, UUID> {
    @Query("SELECT p FROM Problem p WHERE " +
            "( LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.statement) LIKE LOWER(CONCAT('%', :search, '%'))) AND p.author.id = :authorId")
    Page<Problem> findByUsersProblemBySearchKey(@Param("search") String search,@Param("authorId") UUID authorId, Pageable pageable);


    @Query("SELECT p FROM Problem p WHERE p.author.id = :authorId")
    Page<Problem> findAllByUsersProblem(@Param("authorId") UUID authorId, Pageable pageable);



}
