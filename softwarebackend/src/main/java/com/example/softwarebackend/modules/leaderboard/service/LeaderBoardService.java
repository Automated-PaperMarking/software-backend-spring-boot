package com.example.softwarebackend.modules.leaderboard.service;

import com.example.softwarebackend.modules.leaderboard.dto.LeaderBoardUpdateDTO;
import jakarta.transaction.Transactional;

public interface LeaderBoardService {
    @Transactional
    void updateLeaderBoard(LeaderBoardUpdateDTO leaderBoardUpdateDTO);
}
