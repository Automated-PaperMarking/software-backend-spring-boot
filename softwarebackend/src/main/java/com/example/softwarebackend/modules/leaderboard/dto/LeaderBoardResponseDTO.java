package com.example.softwarebackend.modules.leaderboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoardResponseDTO {
    private String contestId;
    private String contestName;
    private List<LeaderBoardEntryResponseDTO> entries;
    private int numberOfParticipants;
}
