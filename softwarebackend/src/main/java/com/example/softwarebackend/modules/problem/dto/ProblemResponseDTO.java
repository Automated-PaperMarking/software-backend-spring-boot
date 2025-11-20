package com.example.softwarebackend.modules.problem.dto;

import com.example.softwarebackend.shared.enums.DifficultyLevel;
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
public class ProblemResponseDTO {
    private String id;
    private String title;
    private String statement;
    private DifficultyLevel difficultyLevel;
    private String contestId;
    private List<TestCaseResponseDTO> testCases;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
