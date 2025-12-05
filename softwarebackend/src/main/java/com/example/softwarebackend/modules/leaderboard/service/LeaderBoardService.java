package com.example.softwarebackend.modules.leaderboard.service;

import com.example.softwarebackend.modules.leaderboard.dto.LeaderBoardEntryResponseDTO;
import com.example.softwarebackend.modules.leaderboard.dto.LeaderBoardUpdateDTO;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import jakarta.transaction.Transactional;

public interface LeaderBoardService {
    @Transactional
    void updateLeaderBoard(LeaderBoardUpdateDTO leaderBoardUpdateDTO);

    //get leader board by contest id
    PageResponseDTO<LeaderBoardEntryResponseDTO> getLeaderBoardByContestId(String contestId, String search, int page, int size, String[] sort);
}
