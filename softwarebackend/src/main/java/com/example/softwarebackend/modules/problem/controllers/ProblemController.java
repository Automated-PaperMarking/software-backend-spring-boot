package com.example.softwarebackend.modules.problem.controllers;

import com.example.softwarebackend.modules.problem.dto.ProblemCreateDTO;
import com.example.softwarebackend.modules.problem.dto.ProblemResponseDTO;
import com.example.softwarebackend.modules.problem.services.ProblemService;
import com.example.softwarebackend.shared.dto.response.ApiResponseDTO;
import com.example.softwarebackend.shared.dto.response.PageResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    // Get all problems
    @GetMapping("/all")
    public ResponseEntity<ApiResponseDTO<?>> getAllProblems(@RequestParam(required = false) String search,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id,asc") String[] sort) {

        var currentPage = problemService.getAllProblems(search, page, size, sort);
        ApiResponseDTO<PageResponseDTO<ProblemResponseDTO>> response = new ApiResponseDTO<>("200", "Problems retrieved successfully", currentPage, true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> getProblemById(@PathVariable String id) {
        ProblemResponseDTO problemResponseDTO = problemService.findById(UUID.fromString(id));
        ApiResponseDTO<ProblemResponseDTO> response = new ApiResponseDTO<>("200", "Problem retrieved successfully", problemResponseDTO, true);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<?>> createProblem(@Valid @RequestBody ProblemCreateDTO problemCreateDTO) {
        problemService.createProblem(problemCreateDTO);
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "Problem created successfully", null, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> deleteProblem(@PathVariable String id) {
        problemService.deleteById(UUID.fromString(id));
        return ResponseEntity.ok(new ApiResponseDTO<>("200", "Problem deleted successfully", null, true));
    }
}
