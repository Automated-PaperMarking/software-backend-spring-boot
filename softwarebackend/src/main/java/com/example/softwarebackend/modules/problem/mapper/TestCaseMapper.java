package com.example.softwarebackend.modules.problem.mapper;

import com.example.softwarebackend.modules.problem.dto.TestCaseCreateDTO;
import com.example.softwarebackend.modules.problem.dto.TestCaseResponseDTO;
import com.example.softwarebackend.shared.entities.Problem;
import com.example.softwarebackend.shared.entities.TestCase;

public class TestCaseMapper {
    public static TestCase toEntity(TestCaseCreateDTO testCaseCreateDTO, Problem problem) {
        return TestCase.builder()
                .input(testCaseCreateDTO.getInput())
                .expectedOutput(testCaseCreateDTO.getExpectedOutput())
                .type(testCaseCreateDTO.getType())
                .problem(problem)
                .build();
    }
    public static TestCaseResponseDTO toDTO(TestCase testCase) {
        return TestCaseResponseDTO.builder()
                .id(testCase.getId().toString())
                .input(testCase.getInput())
                .expectedOutput(testCase.getExpectedOutput())
                .type(testCase.getType().toString())
                .problemId(testCase.getProblem().getId().toString())
                .build();
    }
}
