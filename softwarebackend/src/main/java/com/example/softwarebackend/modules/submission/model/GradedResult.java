package com.example.softwarebackend.modules.submission.model;

import com.example.softwarebackend.shared.enums.GradingResultStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradedResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull
    private String studentId;
    @NotNull
    private double understandingLogic;
    @NotNull
    private double correctnessScore;
    @NotNull
    private double readabilityScore;
    @NotNull
    private double totalScore;
    @NotNull
    @Enumerated(EnumType.STRING)
    private GradingResultStatus gradingResultStatus;


}
