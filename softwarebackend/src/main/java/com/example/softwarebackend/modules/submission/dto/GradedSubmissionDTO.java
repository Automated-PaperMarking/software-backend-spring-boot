package com.example.softwarebackend.modules.submission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradedSubmissionDTO {

    private String submissionId;
    private double understandingLogic;
    private double correctnessScore;
    private double readabilityScore;
    private double totalScore;


}
