package com.example.softwarebackend.modules.constest.controllers;

import com.example.softwarebackend.modules.constest.dto.AddProblemsToContestDTO;
import com.example.softwarebackend.modules.constest.dto.ContestCreateDTO;
import com.example.softwarebackend.modules.constest.dto.ContestResponseDTO;
import com.example.softwarebackend.modules.constest.services.ContestService;
import com.example.softwarebackend.shared.dto.response.ApiResponseDTO;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/contests")
@RequiredArgsConstructor
public class ContestController {
    private final ContestService contestService;
    //get all contests
    @GetMapping("/all")
    public ResponseEntity<ApiResponseDTO<?>> getAllProjects(@RequestParam(required = false) String search, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id,asc") String[] sort) {

        var currentPage = contestService.getAllContests(search, page, size, sort);
        ApiResponseDTO<PageResponseDTO<ContestResponseDTO>> response = new ApiResponseDTO<>("200", "Projects retrieved successfully", currentPage, true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> getContestById(@RequestParam String id) {
        ContestResponseDTO contestResponseDTO = contestService.findById(UUID.fromString(id));
        ApiResponseDTO<ContestResponseDTO> response = new ApiResponseDTO<>("200", "Contest retrieved successfully", contestResponseDTO, true);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<?>> createContest(@Valid @RequestBody ContestCreateDTO contestCreateDTO) {
        contestService.createContest(contestCreateDTO);
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "Contest Creation is Successfully", null, false));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseDTO<?>> deleteContest(@RequestParam String id) {
        contestService.deleteById(UUID.fromString(id));
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "Delete Contest endpoint not implemented yet", null, false));
    }

    @PostMapping("/assign-problems")
    public ResponseEntity<ApiResponseDTO<?>> assignProblemsToContest(@Valid @RequestBody AddProblemsToContestDTO problemsDTO) {
        contestService.assignProblemsToContest(problemsDTO);
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "Problems assigned to contest successfully", null, true));
    }
}
