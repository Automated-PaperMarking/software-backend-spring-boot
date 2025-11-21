package com.example.softwarebackend.modules.constest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProblemsToContestDTO {
    @NotBlank(message = "Contest ID cannot be blank")
    private String contestId;
    @NotEmpty(message = "Problem IDs list cannot be empty")
    private List<String> problemIds;
}
