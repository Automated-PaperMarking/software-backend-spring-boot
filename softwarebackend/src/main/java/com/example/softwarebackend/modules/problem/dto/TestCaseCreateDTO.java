package com.example.softwarebackend.modules.problem.dto;

import com.example.softwarebackend.shared.enums.TestCaseType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseCreateDTO {
    @NotBlank(message = "Input is mandatory")
    private String input;
    @NotBlank(message = "Expected output is mandatory")
    private String expectedOutput;
    @NotBlank(message = "Type is mandatory")
    private TestCaseType type;
}
