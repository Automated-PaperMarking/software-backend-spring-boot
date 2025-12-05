package com.example.softwarebackend.modules.leaderboard.controller;

import com.example.softwarebackend.modules.leaderboard.dto.LeaderBoardEntryResponseDTO;
import com.example.softwarebackend.modules.leaderboard.service.LeaderBoardService;
import com.example.softwarebackend.shared.dto.response.ApiResponseDTO;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/leaderboard")
public class LeaderBoardController {
    private final LeaderBoardService leaderBoardService;

    //get leaderboard by contest id
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<LeaderBoardEntryResponseDTO>>> getLeaderBoardByContestId(@RequestParam String contestId, @RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id,asc") String[] sort ) {
        var currentPage = leaderBoardService.getLeaderBoardByContestId(contestId,search, page, size, sort);
        ApiResponseDTO<PageResponseDTO<LeaderBoardEntryResponseDTO>> response = new ApiResponseDTO<>("200", "Projects retrieved successfully", currentPage, true);
        return ResponseEntity.ok(response);
    }


}
