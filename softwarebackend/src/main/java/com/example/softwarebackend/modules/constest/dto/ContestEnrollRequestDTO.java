package com.example.softwarebackend.modules.constest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContestEnrollRequestDTO {
    @NotBlank(message = "Enrollment key cannot be blank")
    private String enrollmentKey;
    @NotBlank(message = "Contest ID cannot be blank")
    private String contestId;
}
