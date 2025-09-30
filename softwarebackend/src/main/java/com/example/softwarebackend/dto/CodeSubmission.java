package com.example.softwarebackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeSubmission {

    private String gradedResultId;
    @NotBlank(message = "Student id is Required")
    private String studentId;
    @NotBlank (message = "SubmissionType is Required")
    private SubmissionType submissionType;
    @NotBlank(message = "Language is Required")
    private String language;
    @NotBlank(message = "Code is Required")
    private String code;
    @NotBlank(message = "At least one Testcase is Required")
    private List<TestCase> testCases;




    @Override
    public String toString() {
        return "CodeSubmission{" +
                "studentId='" + studentId + '\'' +
                ", submissionType=" + submissionType +
                ", language='" + language + '\'' +
                ", code='" + (code != null ? code.substring(0, Math.min(code.length(), 50)) + "..." : null) + '\'' +
                ", testCases=" + testCases +
                '}';
    }

}
