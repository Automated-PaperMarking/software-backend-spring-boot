package com.example.softwarebackend.modules.grader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponseDTO {

    private String gradedResultId;
    private String studentId;
    private double understandingLogic;
    private double correctnessScore;
    private double readabilityScore;
    private double totalScore;
    private String gradingResultStatus;

}
