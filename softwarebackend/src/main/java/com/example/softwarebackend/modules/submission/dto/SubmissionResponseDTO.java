package com.example.softwarebackend.modules.submission.dto;
import com.example.softwarebackend.shared.enums.SubmissionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionResponseDTO {

    private String id;

    private String code;

    private SubmissionType submissionType;

    private String language;

    private String studentId;

    private String problemId;

    private double understandingLogic;

    private double correctnessScore;

    private double readabilityScore;

    private double totalScore;

    private String gradingResultStatus;

    private String createdAt;

    private String updatedAt;
}
