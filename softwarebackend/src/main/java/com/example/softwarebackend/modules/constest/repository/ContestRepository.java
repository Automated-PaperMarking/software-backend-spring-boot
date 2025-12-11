package com.example.softwarebackend.modules.constest.repository;


import com.example.softwarebackend.shared.entities.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContestRepository extends JpaRepository<Contest, UUID> {
    @Query("SELECT c FROM Contest c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Contest> findBySearchKey(@Param("search") String search, Pageable pageable);

    @Query("SELECT c FROM Contest c WHERE c.author.id = :authorId AND " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Contest> findByAuthorIdAndSearchKey(@Param("authorId") UUID authorId, @Param("search") String search, Pageable pageable);

    Page<Contest> findByAuthorId(UUID authorId, Pageable pageable);
}
