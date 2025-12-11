package com.example.softwarebackend.modules.leaderboard.mapper;

import com.example.softwarebackend.modules.leaderboard.dto.LeaderBoardEntryResponseDTO;
import com.example.softwarebackend.shared.entities.LeaderBoardEntry;

public class LeaderBoardMapper {
    public static LeaderBoardEntryResponseDTO toDTO(LeaderBoardEntry leaderBoardEntry) {
        return LeaderBoardEntryResponseDTO.builder()
                .id(leaderBoardEntry.getId().toString())
                .studentName(leaderBoardEntry.getUser().getFirstName() + " " + leaderBoardEntry.getUser().getLastName())
                .studentId(leaderBoardEntry.getUser().getId().toString())
                .totalScore(leaderBoardEntry.getTotalScore())
                .rank(leaderBoardEntry.getRank())
                .problemsSolved(leaderBoardEntry.getProblemsSolved())
                .lastSubmissionTime(leaderBoardEntry.getUpdatedAt().toString())
                .build();
    }
}
