package com.example.softwarebackend.modules.problem.mapper;

import com.example.softwarebackend.modules.problem.dto.ProblemCreateDTO;
import com.example.softwarebackend.modules.problem.dto.ProblemResponseDTO;
import com.example.softwarebackend.shared.entities.Problem;

public class ProblemMapper {

    public static Problem toEntity(ProblemCreateDTO dto) {
        Problem problem = Problem.builder()
                .title(dto.getTitle())
                .statement(dto.getStatement())
                .difficultyLevel(dto.getDifficultyLevel())
                .build();

        // Map test cases with the problem reference to establish bidirectional relationship
        problem.setTestCases(dto.getTestCases().stream()
                .map(testCaseDto -> TestCaseMapper.toEntity(testCaseDto, problem))
                .toList());

        return problem;
    }

    public static ProblemResponseDTO toDTO(Problem problem) {
        return ProblemResponseDTO.builder()
                .id(problem.getId().toString())
                .title(problem.getTitle())
                .statement(problem.getStatement())
                .difficultyLevel(problem.getDifficultyLevel())
                .testCases(problem.getTestCases().stream().map(TestCaseMapper::toDTO).toList())
                .createdAt(problem.getCreatedAt().toString())
                .updatedAt(problem.getUpdatedAt().toString())
                .build();
    }
}
