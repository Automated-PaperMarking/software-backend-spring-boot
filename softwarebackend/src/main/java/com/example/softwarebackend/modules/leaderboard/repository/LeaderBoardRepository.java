package com.example.softwarebackend.modules.leaderboard.repository;

import com.example.softwarebackend.shared.entities.LeaderBoardEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaderBoardRepository extends JpaRepository<LeaderBoardEntry, UUID> {

    @Query("SELECT l FROM LeaderBoardEntry l WHERE l.user.id = :studentId AND l.contest.id = :contestId ORDER BY l.totalScore DESC, l.problemsSolved DESC")
    Optional<LeaderBoardEntry> findByStudentIdAndContestIdOrderByScoreDesc(@Param("studentId") UUID studentId, @Param("contestId") UUID contestId);

    @Query("SELECT l FROM LeaderBoardEntry l WHERE l.contest.id = :contestId AND " +
            "(LOWER(CONCAT(l.user.firstName, ' ', l.user.lastName)) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(l.user.email) LIKE LOWER(CONCAT('%', :search, '%'))) ORDER BY l.totalScore DESC, l.problemsSolved DESC"
    )
    Page<LeaderBoardEntry> findBySearchKeyOrderByScoreDesc(@Param("contestId") UUID contestId, @Param("search") String search, Pageable pageable);

    @Query("SELECT l FROM LeaderBoardEntry l WHERE l.contest.id = :contestId")
    Page<LeaderBoardEntry> findAllByContestId(@Param("contestId") UUID contestId, Pageable pageable);

    @Query("SELECT l FROM LeaderBoardEntry l WHERE l.contest.id = :contestId ORDER BY l.totalScore DESC, l.problemsSolved DESC")
    List<LeaderBoardEntry> findAllByContestIdOrderByScoreDesc(@Param("contestId") UUID contestId);
}
