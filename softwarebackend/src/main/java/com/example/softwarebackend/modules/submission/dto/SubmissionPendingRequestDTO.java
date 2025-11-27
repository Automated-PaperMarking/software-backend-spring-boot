package com.example.softwarebackend.modules.submission.dto;

import com.example.softwarebackend.shared.enums.SubmissionType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionPendingRequestDTO {

    @NotBlank(message = "Submission ID cannot be blank")
    private String submissionId;

    @NotBlank(message = "Language cannot be blank")
    private String language;

    @NotBlank(message = "Code cannot be blank")
    private String code;

    @NotBlank(message = "Problem cannot be blank")
    private String problem;

    @NotBlank(message = "Submission type cannot be blank")
    private SubmissionType submissionType;

}
