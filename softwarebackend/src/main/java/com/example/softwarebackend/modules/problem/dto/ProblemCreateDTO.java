package com.example.softwarebackend.modules.problem.dto;

import com.example.softwarebackend.shared.enums.DifficultyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemCreateDTO {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Statement is mandatory")
    private String statement;

    @NotNull(message = "Difficulty level is mandatory")
    private DifficultyLevel difficultyLevel;

    @NotNull(message = "Test cases are mandatory")
    private List<TestCaseCreateDTO> testCases;
}
