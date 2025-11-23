package com.example.softwarebackend.modules.submission.dto;


import com.example.softwarebackend.shared.enums.SubmissionType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionCreateRequestDTO {

    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotBlank(message = "Submission type cannot be blank")
    private SubmissionType submissionType;

    @NotBlank(message = "Language cannot be blank")
    private String language;

    @NotBlank(message = "Student ID cannot be blank")
    private String studentId;

    @NotBlank(message = "Problem ID cannot be blank")
    private String problemId;

    @NotBlank(message = "Contest ID cannot be blank")
    private String contestId;

}
