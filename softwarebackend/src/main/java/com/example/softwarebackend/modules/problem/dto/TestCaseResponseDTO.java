package com.example.softwarebackend.modules.problem.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseResponseDTO {
    private String id;
    private String input;
    private String expectedOutput;
    private String type;
    private String problemId;
}
