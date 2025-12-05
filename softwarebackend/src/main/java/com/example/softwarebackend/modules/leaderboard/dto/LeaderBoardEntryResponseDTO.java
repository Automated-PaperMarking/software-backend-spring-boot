package com.example.softwarebackend.modules.leaderboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoardEntryResponseDTO {
    private String id;
    private String studentName;
    private double totalScore;
    private int rank;
    private int problemsSolved;
    private String lastSubmissionTime;

}
