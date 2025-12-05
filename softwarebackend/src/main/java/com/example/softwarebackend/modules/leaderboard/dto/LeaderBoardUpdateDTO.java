package com.example.softwarebackend.modules.leaderboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoardUpdateDTO {
    private UUID studentId;
    private UUID contestId;
    private UUID problemId;
    private double totalScoreForSubmission;
}
